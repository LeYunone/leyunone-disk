package xyz.leyuna.disk.service.file;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.binary.Base64;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import xyz.leyuna.disk.command.CacheExe;
import xyz.leyuna.disk.domain.domain.FileInfoE;
import xyz.leyuna.disk.domain.domain.FileUpLogE;
import xyz.leyuna.disk.domain.domain.FileUserE;
import xyz.leyuna.disk.model.DataResponse;
import xyz.leyuna.disk.model.co.FileInfoCO;
import xyz.leyuna.disk.model.co.FileUpLogCO;
import xyz.leyuna.disk.model.co.FileUserCO;
import xyz.leyuna.disk.model.constant.ServerCode;
import xyz.leyuna.disk.model.dto.file.UpFileDTO;
import xyz.leyuna.disk.model.enums.ErrorEnum;
import xyz.leyuna.disk.model.enums.FileEnum;
import xyz.leyuna.disk.model.enums.SortEnum;
import xyz.leyuna.disk.util.AssertUtil;
import xyz.leyuna.disk.util.FileUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author LeYuna
 * @email 365627310@qq.com
 * @create 2021-12-24 16:55
 * 文件相关服务 [非查询]
 */
@Service
@Log4j2
public class FileService {

    @Autowired
    private CacheExe cacheExe;

    //默认最大520000
    @Value("${disk.max.memory:520000}")
    private Long maxMemory;

    //默认大小5000
    @Value("${disk.max.file:5000}")
    private Long maxFile;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

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

                //判断本请求是否是最后的分片，如果是最后的分片则进行合并
                Integer size = sliceFile.getParentFile().listFiles().length;
                if (size.equals(upFileDTO.getSliceAll())) {
                    //合并
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
                    AssertUtil.isFalse(ObjectUtil.isEmpty(fileUpLogCO),ErrorEnum.FILE_UPLOAD_FILE.getName());
                    FileUpLogE.queryInstance().setId(fileUpLogCO.getId())
                            .setUpFileTotalSize(fileUpLogCO.getUpFileTotalSize()+upFileDTO.getFileSize()).update();

                    //上传完成，删除临时目录
                    this.deleteSliceTemp(tempPath);

                    //删除成功，清除redis
                    cacheExe.clearFileMD5(userId,fileKey);

                    //开启计时保存功能
                    if(StrUtil.isNotBlank(upFileDTO.getSaveTime())){
                        cacheExe.setSaveTimeFileCache(saveId,userId,upFileDTO.getSaveTime());
                    }
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

    /**
     * 上传文件 【最普通模式】
     *
     * @param upFileDTO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public DataResponse savaFile(UpFileDTO upFileDTO) {
        //上传用户编号
        String userId = upFileDTO.getUserId();
        try {
            MultipartFile file = upFileDTO.getFile();
            String name = file.getOriginalFilename();
            String type = name.substring(name.lastIndexOf('.') + 1);
            //文件类型
            FileEnum fileEnum = FileEnum.loadType(type);

            //计算K级内存大小
            Long fileSize = file.getSize();
            AssertUtil.isFalse(fileSize > maxMemory, ErrorEnum.FILE_USER_OVER.getName());
            //用户的文件列表
            List<FileUpLogCO> fileUpLogCOS = FileUpLogE.queryInstance().setUserId(userId).selectByConOrder(SortEnum.UPDATE_DESC);
            FileUpLogCO lastFile = null;
            if (CollectionUtil.isNotEmpty(fileUpLogCOS)) {
                lastFile = fileUpLogCOS.get(0);
                //大于5G非法
                if (lastFile.getUpFileTotalSize() + fileSize > maxMemory) {
                    //用户列表内存已满，无法继续上传文件
                    FileUpLogE.queryInstance()
                            .setId(lastFile.getId())
                            .setUpSign(1).update();
                    AssertUtil.isFalse(true, ErrorEnum.FILE_USER_OVER.getName());
                }
            }
            //当前操作后用户的所有文件内存
            Long sizetotal = ObjectUtil.isEmpty(lastFile) ? fileSize : lastFile.getUpFileTotalSize() + fileSize;

            String saveId = FileInfoE.queryInstance()
                    .setFileSize(fileSize)
                    .setSaveDt(StrUtil.isEmpty(upFileDTO.getSaveTime()) ? "永久保存" : upFileDTO.getSaveTime())
                    .setName(file.getOriginalFilename())
                    .setFileType(fileEnum.getValue()).save();
            //如果保存的文件非永久，则进行一个度的校验
            if (StrUtil.isNotEmpty(upFileDTO.getSaveTime())) {

                DateTimeFormatter saveTime = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate ld = LocalDate.parse(upFileDTO.getSaveTime(), saveTime);
                LocalDateTime ldt = LocalDateTime.of(ld, LocalDateTime.now().toLocalTime());
                Duration duration = Duration.between(LocalDateTime.now(), ldt);
                long saveSec = duration.getSeconds();
                //如果文件大小符合条件存入缓存，则进人redis流程
                if (fileSize <= maxFile) {
                    String base64 = null;
                    base64 = Base64.encodeBase64String(file.getBytes());

                    AssertUtil.isFalse(StrUtil.isEmpty(base64), ErrorEnum.FILE_UPLOAD_FILE.getName());
                    //计算存储时间 将小量文件存入redis中进行保存
                    cacheExe.setCacheKey("disk_file:" + saveId, base64, saveSec);
                    //TODO 缓存过期事件，更新数据库
                } else {
                    //TODO 走定时任务，到过期时间时，将存储的文件删除

                    //暂时进行文件存储  在redis中添加一个记号
                    cacheExe.setCacheKey("disk_deleted:" + saveId, "DELETED", saveSec);

                    File saveFile = new File(ServerCode.FILE_ADDRESS + userId + "/" + fileEnum.getName() + "/" + name);
                    if (!saveFile.getParentFile().exists()) {
                        saveFile.getParentFile().mkdirs();
                    }
                    file.transferTo(saveFile);
                }
            } else {
                //如果需要的是永久保存 如果小于5G 则进行累计计算，并将文件转入磁盘中
                File saveFile = new File(ServerCode.FILE_ADDRESS + userId + "/" + fileEnum.getName() + "/" + name);
                if (!saveFile.getParentFile().exists()) {
                    saveFile.getParentFile().mkdirs();
                }
                file.transferTo(saveFile);
            }
            if (null == lastFile) {
                FileUpLogE.queryInstance().setUpFileTotalSize(sizetotal).setUpSign(0).setUserId(userId).save();
            } else {
                FileUpLogE.queryInstance().setId(lastFile.getId()).setUpFileTotalSize(sizetotal).update();
            }
        } catch (IOException e) {
            log.error(e);
            AssertUtil.isFalse(true, ErrorEnum.SERVER_ERROR.getName());
        }
        return DataResponse.buildSuccess();
    }

    /**
     * 删除文件
     *
     * @param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public DataResponse deleteFile(String fileId, String userId) {

        //检查本文件是否属于操作用户
        FileUserCO fileUserCO = FileUserE.queryInstance().setFileId(fileId).setUserId(userId).selectOne();
        AssertUtil.isFalse(ObjectUtil.isEmpty(fileUserCO), ErrorEnum.USER_INFO_ERROR.getName());

        //检查用户上传目录是否存在/正常
        FileUpLogCO fileUpLogCO = FileUpLogE.queryInstance().setUserId(userId).selectOne();
        AssertUtil.isFalse(ObjectUtil.isEmpty(fileUpLogCO), ErrorEnum.SELECT_NOT_FOUND.getName());

        //逻辑删除数据条
        FileUserE.queryInstance().setId(fileUserCO.getId()).setDeleted(1).update();

        //物理删除文件:先检查这个文件是否除了自己 所有用户都不可用了
        List<FileUserCO> fileUserCOS = FileUserE.queryInstance().setFileId(fileId).selectByCon();
        FileInfoCO fileInfoCO = FileInfoE.queryInstance().setId(fileId).selectById();
        if (fileUserCOS.size() == 1) {
            //删除本文件
            FileUtil.deleteFile(fileInfoCO.getFilePath());
        }

        //计算用户的新内存总值
        Long newSize = fileUpLogCO.getUpFileTotalSize() - fileInfoCO.getFileSize();
        FileUpLogE.queryInstance().setId(fileUpLogCO.getId()).setUpFileTotalSize(newSize).update();
        //删除完成
        return DataResponse.buildSuccess();
    }

    /**
     * 获取文件
     * 断点下载 ， 感觉最适合WEB端的云盘应用。如果使用分片的话，要处理的点太多太多了。
     *
     * @param fileId 文件id
     * @return
     */
    public FileInfoCO getFile(String fileId, String userId) {

        //检查本文件是否属于操作用户
        FileUserCO fileUserCO = FileUserE.queryInstance().setFileId(fileId).setUserId(userId).selectOne();
        AssertUtil.isFalse(ObjectUtil.isEmpty(fileUserCO), ErrorEnum.USER_INFO_ERROR.getName());

        //获取文件数据
        FileInfoCO fileInfoCO = FileInfoE.queryInstance().setId(fileId).selectById();
        AssertUtil.isFalse(ObjectUtil.isEmpty(fileInfoCO), ErrorEnum.SELECT_NOT_FOUND.getName());

        File file = FileUtil.getFile(fileInfoCO.getFilePath());
        AssertUtil.isFalse(ObjectUtil.isEmpty(file), ErrorEnum.SELECT_NOT_FOUND.getName());

        //文件转换成数组byte
        byte[] bytes = FileUtil.FiletoByte(file);
        fileInfoCO.setBase64File(bytes);
        return fileInfoCO;
    }
}
