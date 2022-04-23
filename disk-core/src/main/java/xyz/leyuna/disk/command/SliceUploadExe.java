package xyz.leyuna.disk.command;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.leyuna.disk.domain.domain.FileInfoE;
import xyz.leyuna.disk.domain.domain.FileUpLogE;
import xyz.leyuna.disk.domain.domain.FileUserE;
import xyz.leyuna.disk.model.DataResponse;
import xyz.leyuna.disk.model.co.FileUpLogCO;
import xyz.leyuna.disk.model.constant.ServerCode;
import xyz.leyuna.disk.model.dto.file.UpFileDTO;
import xyz.leyuna.disk.model.enums.ErrorEnum;
import xyz.leyuna.disk.util.AssertUtil;

import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.locks.LockSupport;

/**
 * @author LeYuna
 * @email 365627310@qq.com
 * @date 2022-04-22
 */
@Service
public class SliceUploadExe {

    @Autowired
    private CacheExe cacheExe;

    /**
     * 分片上传
     *
     * @return
     */
    public DataResponse sliceUpload(UpFileDTO upFileDTO) {
        String userId = upFileDTO.getUserId();
        //文件key
        String fileKey = upFileDTO.getFileKey();
        //本次文件的MD5码
        String fileMD5Value = cacheExe.getFileMD5Value(userId, fileKey);
        AssertUtil.isFalse(StrUtil.isBlank(fileMD5Value), ErrorEnum.FILE_UPLOAD_FILE.getName());

        //获得分片文件存储的临时目录
        String tempPath = this.resoleSliceTempPath(fileMD5Value);
        AssertUtil.isFalse(StrUtil.isBlank(tempPath), ErrorEnum.FILE_UPLOAD_FILE.getName());

        //开始进行切片化上传
        File sliceFile = new File(tempPath + upFileDTO.getSliceIndex());
        //如果这个片在历史中已经完成，则跳过
        if (!sliceFile.exists()) {
            FileOutputStream fos = null;
            InputStream inputStream = null;
            try {
                fos = new FileOutputStream(sliceFile);
                //本次上传文件
                inputStream = upFileDTO.getFile().getInputStream();
                //写入文件
                IOUtils.copy(inputStream, fos);

                //如果不是最终分片，则判断本次分片是否是上传的最后一个分片
                if (!upFileDTO.getSliceIndex().equals(upFileDTO.getSliceAll()) &&
                        sliceFile.getParentFile().listFiles().length == upFileDTO.getSliceAll()) {
                    //打开阻塞中的最终分片
                    LockSupport.unpark(ServerCode.threadUpload.get(fileMD5Value));
                }

                //判断本请求是否是最后的分片，如果是最后的分片则进行合并
                if (upFileDTO.getSliceIndex().equals(upFileDTO.getSliceAll())) {

                    //如果其他分片还没到达，则进挂起
                    if (upFileDTO.getSliceAll() != sliceFile.getParentFile().listFiles().length) {
                        fos.close();
                        inputStream.close();
                        ServerCode.threadUpload.put(fileMD5Value, Thread.currentThread());
                        LockSupport.park();
                    }
                    //合并文件
                    String filePath = this.mergeSliceFile(tempPath, upFileDTO.getFileName());

                    //保存文件信息
                    String saveId = FileInfoE.queryInstance().setFilePath(filePath).
                            setFileSize(upFileDTO.getFileSize()).setFileType(upFileDTO.getFileType())
                            .setName(upFileDTO.getFileName())
                            .setSaveDt(StrUtil.isEmpty(upFileDTO.getSaveTime()) ? "永久保存" : upFileDTO.getSaveTime()).save();
                    //加载到用户文件列表上
                    FileUserE.queryInstance().setUserId(userId).setFileId(saveId).save();

                    //计算用户新内存
                    FileUpLogCO fileUpLogCO = FileUpLogE.queryInstance().setUserId(userId).selectOne();
                    AssertUtil.isFalse(ObjectUtil.isEmpty(fileUpLogCO), ErrorEnum.FILE_UPLOAD_FILE.getName());
                    FileUpLogE.queryInstance().setId(fileUpLogCO.getId())
                            .setUpFileTotalSize(fileUpLogCO.getUpFileTotalSize() + upFileDTO.getFileSize()).update();

                    //上传完成，删除临时目录
                    this.deleteSliceTemp(tempPath);

                    //删除成功，清除redis
                    cacheExe.clearFileMD5(userId, fileKey);

                    //开启计时保存功能
                    if (StrUtil.isNotBlank(upFileDTO.getSaveTime())) {
                        cacheExe.setSaveTimeFileCache(saveId, userId, upFileDTO.getSaveTime());
                    }

                    //删除分片的记录
                    ServerCode.threadUpload.remove(fileMD5Value);
                }
            } catch (Exception e) {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
        return DataResponse.buildSuccess();
    }

    /**
     * 删除临时目录
     *
     * @param tempPath
     */
    private void deleteSliceTemp(String tempPath) {
        File file = new File(tempPath);
        AssertUtil.isFalse(ObjectUtil.isEmpty(file), ErrorEnum.FILE_UPLOAD_FILE.getName());
        file.delete();
    }

    /**
     * 处理出临时目录
     *
     * @param fileMD5Value
     * @return
     */
    private String resoleSliceTempPath(String fileMD5Value) {
        String tempPath = ServerCode.TEMP_PATH + fileMD5Value + "/";
        File tempFile = new File(tempPath);
        if (tempFile.exists()) {
            return tempPath;
        }
        boolean mkdirs = tempFile.mkdirs();
        return mkdirs ? tempPath : null;
    }

    /**
     * 合并分片
     *
     * @param tempPath
     * @param fileName
     */
    private String mergeSliceFile(String tempPath, String fileName) {
        //分片文件的临时目录
        File sliceFile = new File(tempPath);
        //本次文件的保存位置
        String savePath = ServerCode.FILE_ADDRESS + fileName;
        File thisFile = new File(savePath);
        //所有分片
        File[] files = sliceFile.listFiles();
        //按照1 2 3 4 排序，有序写入
        Arrays.stream(files).sorted(Comparator.comparing(o -> Integer.valueOf(o.getName())));
        RandomAccessFile randomAccessFile = null;
        try {
            //使用RandomAccessFile 达到追加插入， 也可以使用Inputstream的Skip方法跳过已读过的
            randomAccessFile = new RandomAccessFile(thisFile, "rw");
            byte[] buffer = new byte[1024];
            for (File file : files) {
                RandomAccessFile randomAccessFileReader = new RandomAccessFile(file, "r");
                int len;
                while ((len = randomAccessFileReader.read(buffer)) != -1) {
                    //追加写入
                    randomAccessFile.write(buffer, 0, len);
                }
                randomAccessFileReader.close();
            }
        } catch (Exception e) {
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
        return savePath;
    }
}
