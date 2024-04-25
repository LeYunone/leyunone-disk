//package com.leyunone.disk.command;
//
//import cn.hutool.core.util.ObjectUtil;
//import cn.hutool.core.util.StrUtil;
//import com.leyunone.disk.domain.domain.FileInfoE;
//import com.leyunone.disk.domain.domain.FileMd5E;
//import com.leyunone.disk.domain.domain.FileUpLogE;
//import com.leyunone.disk.domain.domain.FileUserE;
//import com.leyunone.disk.model.DataResponse;
//import com.leyunone.disk.model.co.FileUpLogCO;
//import com.leyunone.disk.common.constant.ServerCode;
//import com.leyunone.disk.model.dto.UpFileDTO;
//import com.leyunone.disk.common.enums.FileTypeEnum;
//import com.leyunone.disk.util.AssertUtil;
//import com.leyunone.disk.util.FileUtil;
//import org.apache.tomcat.util.http.fileupload.IOUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.io.*;
//import java.util.Arrays;
//import java.util.Comparator;
//import java.util.concurrent.locks.LockSupport;
//
///**
// * @author LeYunone
// * @email 365627310@qq.com
// * @date 2022-04-22
// */
//@Service
//public class SliceUploadExe {
//
//    @Autowired
//    private CacheExe cacheExe;
//
//    /**
//     * 分片上传
//     *
//     * @return
//     */
//    @Transactional(rollbackFor = Exception.class)
//    public DataResponse sliceUpload(UpFileDTO upFileDTO) {
//        String userId = upFileDTO.getUserId();
//        //本次文件的MD5码
//        String fileMD5Value = upFileDTO.getIdentifier();
//        AssertUtil.isFalse(StrUtil.isBlank(fileMD5Value), ErrorEnum.FILE_UPLOAD_FILE.getName());
//
//        //获得分片文件存储的临时目录
//        String tempPath = FileUtil.resoleFileTempPath(fileMD5Value);
//        AssertUtil.isFalse(StrUtil.isBlank(tempPath), ErrorEnum.FILE_UPLOAD_FILE.getName());
//
//        //开始进行切片化上传
//        File sliceFile = new File(tempPath + upFileDTO.getChunkNumber());
//        //如果这个片在历史中已经完成，则跳过 双重校验
//        if (!sliceFile.exists()) {
//            FileOutputStream fos = null;
//            InputStream inputStream = null;
//            try {
//                fos = new FileOutputStream(sliceFile);
//                //本次上传文件
//                inputStream = upFileDTO.getFile().getInputStream();
//                //写入文件
//                IOUtils.copy(inputStream, fos);
//
//                //如果不是最终分片，但是是总文件的最后一个分片，则放开合并文件线程
//                if (!upFileDTO.getChunkNumber().equals(upFileDTO.getTotalChunks()) &&
//                        sliceFile.getParentFile().listFiles().length == upFileDTO.getTotalChunks()) {
//                    //打开阻塞中的最终分片
//                    LockSupport.unpark(ServerCode.threadUpload.get(fileMD5Value));
//                }
//
//                //判断本请求是否是最后的分片，如果是最后的分片则进行合并
//                if (upFileDTO.getChunkNumber().equals(upFileDTO.getTotalChunks())) {
//
//                    //如果其他分片还没到达，则进挂起
//                    if (upFileDTO.getTotalChunks() != sliceFile.getParentFile().listFiles().length) {
//                        fos.close();
//                        inputStream.close();
//                        ServerCode.threadUpload.put(fileMD5Value, Thread.currentThread());
//                        LockSupport.park();
//                    }
//                    //合并文件
//                    FileTypeEnum fileTypeEnum = FileTypeEnum.loadType(upFileDTO.getFileType());
//                    String filePath = this.mergeSliceFile(tempPath, upFileDTO.getFile().getOriginalFilename(), fileTypeEnum.getName());
//                    Integer fileType = fileTypeEnum.getValue();
//                    //保存文件信息
//                    String fileId = FileInfoE.queryInstance().setFilePath(filePath).
//                            setFileSize(upFileDTO.getTotalSize())
//                            .setFileType(fileType)
//                            .setName(upFileDTO.getFilename())
//                            .setSaveDt(StrUtil.isEmpty(upFileDTO.getSaveTime()) ? "永久保存" : upFileDTO.getSaveTime()).save();
//                    //加载到用户文件列表上
//                    FileUserE.queryInstance().setUserId(userId).setFileId(fileId).setFileFolderId(upFileDTO.getFileFolderId()).save();
//
//                    //保存改文件的MD5码记录
//                    FileMd5E.queryInstance().setFileId(fileId).setMd5Code(fileMD5Value).save();
//
//                    //计算用户新内存
//                    FileUpLogCO fileUpLogCO = FileUpLogE.queryInstance().setUserId(userId).selectOne();
//                    AssertUtil.isFalse(ObjectUtil.isEmpty(fileUpLogCO), ErrorEnum.FILE_UPLOAD_FILE.getName());
//                    FileUpLogE.queryInstance().setId(fileUpLogCO.getId())
//                            .setUpFileTotalSize(fileUpLogCO.getUpFileTotalSize() + upFileDTO.getTotalSize()).update();
//
//                    //开启计时保存功能
//                    if (StrUtil.isNotBlank(upFileDTO.getSaveTime())) {
//                        cacheExe.setSaveTimeFileCache(fileId, userId, upFileDTO.getSaveTime());
//                    }
//
//                    //删除分片的记录
//                    ServerCode.threadUpload.remove(fileMD5Value);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }finally {
//                if (fos != null) {
//                    try {
//                        fos.close();
//                    } catch (IOException ioException) {
//                        ioException.printStackTrace();
//                    }
//                }
//                if (inputStream != null) {
//                    try {
//                        inputStream.close();
//                    } catch (IOException ioException) {
//                        ioException.printStackTrace();
//                    }
//                }
//            }
//        }
//        //上传成功，返回临时目录位置 等待删除
//        return DataResponse.of(tempPath);
//    }
//
//    /**
//     * 删除临时目录
//     *
//     * @param tempPath
//     */
//    public void deleteSliceTemp(String tempPath) {
//        File file = new File(tempPath);
//        AssertUtil.isFalse(ObjectUtil.isEmpty(file), ErrorEnum.FILE_UPLOAD_FILE.getName());
//        //2022-4-26版本 临时目录下文件夹设计为单一
//        if(file.exists()){
//            File[] files = file.listFiles();
//            for(File cfile : files){
//                AssertUtil.isFalse(!cfile.delete(),ErrorEnum.FILE_UPLOAD_FILE.getName());
//
//            }
//        }
//        AssertUtil.isFalse(!file.delete(),ErrorEnum.FILE_UPLOAD_FILE.getName());
//    }
//
//    /**
//     * 合并分片
//     *
//     * @param tempPath
//     * @param fileName
//     */
//    private String mergeSliceFile(String tempPath, String fileName,String fileType) {
//        //分片文件的临时目录
//        File sliceFile = new File(tempPath);
//        //本次文件的保存位置
//        String savePath = ServerCode.FILE_ADDRESS +fileType+"/"+fileName;
//        File thisFile = new File(savePath);
//        //所有分片
//        File[] files = sliceFile.listFiles();
//        //按照1 2 3 4 排序，有序写入
//        Arrays.stream(files).sorted(Comparator.comparing(o -> Integer.valueOf(o.getName())));
//        RandomAccessFile randomAccessFile = null;
//        try {
//            //使用RandomAccessFile 达到追加插入， 也可以使用Inputstream的Skip方法跳过已读过的
//            randomAccessFile = new RandomAccessFile(thisFile, "rw");
//            byte[] buffer = new byte[1024];
//            for (File file : files) {
//                RandomAccessFile randomAccessFileReader = new RandomAccessFile(file, "r");
//                int len;
//                while ((len = randomAccessFileReader.read(buffer)) != -1) {
//                    //追加写入
//                    randomAccessFile.write(buffer, 0, len);
//                }
//                randomAccessFileReader.close();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }finally {
//            if (randomAccessFile != null) {
//                try {
//                    randomAccessFile.close();
//                } catch (IOException ioException) {
//                    ioException.printStackTrace();
//                }
//            }
//        }
//        return savePath;
//    }
//}
