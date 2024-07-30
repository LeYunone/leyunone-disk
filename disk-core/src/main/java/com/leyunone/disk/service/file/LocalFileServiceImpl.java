package com.leyunone.disk.service.file;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.leyunone.disk.common.UploadContext;
import com.leyunone.disk.dao.entry.FileFolderDO;
import com.leyunone.disk.dao.entry.FileInfoDO;
import com.leyunone.disk.dao.repository.FileFolderDao;
import com.leyunone.disk.dao.repository.FileInfoDao;
import com.leyunone.disk.model.ResponseCode;
import com.leyunone.disk.model.bo.UploadBO;
import com.leyunone.disk.model.dto.RequestUploadDTO;
import com.leyunone.disk.model.dto.UpFileDTO;
import com.leyunone.disk.model.vo.DownloadFileVO;
import com.leyunone.disk.service.FileHelpService;
import com.leyunone.disk.service.FileHistoryService;
import com.leyunone.disk.util.AssertUtil;
import com.leyunone.disk.util.CollectionFunctionUtils;
import com.leyunone.disk.util.FileUtil;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * :)
 *
 * @Author LeYunone
 * @Date 2024/4/22 17:42
 */
public class LocalFileServiceImpl extends AbstractFileService {

    @Autowired
    private FileHelpService fileHelpService;
    @Autowired
    private HttpServletResponse httpServletResponse;
    @Value("${disk.fileAddress}")
    private String fileAddress;
    @Value("${disk.download.count:4}")
    private Integer threadCount;

    public LocalFileServiceImpl(FileInfoDao fileInfoDao, FileFolderDao fileFolderDao, FileHistoryService fileHistoryService) {
        super(fileInfoDao, fileFolderDao, fileHistoryService);
    }

    /**
     * 临时目录
     *
     * @param md5
     * @return
     */
    public String resoleFileTempPath(String md5) {
        String tempPath = fileAddress + "/" + md5 + "/";
        File tempFile = new File(tempPath);
        if (tempFile.exists()) {
            return tempPath;
        }
        boolean mkdirs = tempFile.mkdirs();
        return mkdirs ? tempPath : null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UploadBO shardUpload(UpFileDTO upFileDTO) {
        UploadBO uploadBO = new UploadBO();
        uploadBO.setSuccess(false);
        //本次文件的MD5码
        String md5 = upFileDTO.getIdentifier();
        AssertUtil.isFalse(StrUtil.isBlank(md5), "md5 is empty");

        //必须求出redis中的PartETags，在分片合成文件中需要以此为依据，合并文件返回最终地址
        UploadContext.Content content = UploadContext.getUpload(upFileDTO.getUploadId());
        AssertUtil.isFalse(ObjectUtil.isNull(content), ResponseCode.UPLOAD_FAIL);
        MultipartFile file = upFileDTO.getFile();
        String tempPath = resoleFileTempPath(md5);
        int currentChunkNo = upFileDTO.getChunkNumber();
        int totalChunks = upFileDTO.getTotalChunks();
        String ossSlicesId = upFileDTO.getUploadId();
        //字节流转换
        Set<Integer> parts = content.getParts();
        boolean merge = false;
        //分片上传
        //非已上传的分片
        if (!parts.contains(currentChunkNo)) {
            //上传分片
            boolean success = this.uploadSlice(upFileDTO, tempPath);
            if (success) {
                parts.add(currentChunkNo);
                //如果是最后一个子分片，将合并线程放开
                if (currentChunkNo != totalChunks && parts.size() == totalChunks) {
                    merge = true;
                }
            }
        }
        //分片编号等于总片数的时候合并文件,如果符合条件则合并文件，否则继续等待
        if (currentChunkNo == totalChunks) {
            //合并文件
            if (parts.size() != totalChunks) {
                //挂起 等待小分片上传完毕
            } else {
                merge = true;
            }

        }
        content.setParts(parts);
        UploadContext.setCache(ossSlicesId, content);
        if (merge) {
            //最后一个分支上传完成
            try {
                String url = this.mergeSliceFile(tempPath, content.getFileKey(), file.getOriginalFilename());
                uploadBO.setSuccess(true);
                uploadBO.setFilePath(url);
                uploadBO.setTotalSize(upFileDTO.getTotalSize());
                uploadBO.setFileName(file.getOriginalFilename());
                uploadBO.setParentId(upFileDTO.getParentId());
                uploadBO.setIdentifier(md5);
            } catch (Exception e) {
                e.printStackTrace();
                uploadBO.setSuccess(false);
            } finally {
                /**
                 * 如果是最后一个该文件的上传请求就清空缓存
                 */
                if (content.getParentIds().size() == 1) {
                    UploadContext.removeCache(ossSlicesId);
                    UploadContext.removeId(md5);
                } else {
                    content.getParentIds().remove(upFileDTO.getParentId());
                }
//                this.deleteSliceTemp(tempPath);
            }
        }
        return uploadBO;
    }

    private boolean uploadSlice(UpFileDTO upFileDTO, String tempPath) {
        boolean success = true;
        //开始进行切片化上传
        File sliceFile = new File(tempPath + upFileDTO.getChunkNumber());
        //如果这个片在历史中已经完成，则跳过 双重校验
        if (!sliceFile.exists()) {
            FileOutputStream fos = null;
            InputStream inputStream = null;
            try {
                fos = new FileOutputStream(sliceFile);
                //本次上传文件
                inputStream = upFileDTO.getFile().getInputStream();
                //写入文件
                IOUtils.copy(inputStream, fos);
            } catch (Exception e) {
                success = false;
            } finally {
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
        return success;
    }

    /**
     * 合并分片
     *
     * @param tempPath
     * @param fileName
     */
    private String mergeSliceFile(String tempPath, String folderName, String fileName) {
        //分片文件的临时目录
        File sliceFile = new File(tempPath);
        //本次文件的保存位置
        folderName = fileAddress + "/" + folderName;
        File folder = new File(folderName);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String savePath = folderName + fileName;
        File thisFile = new File(savePath);
        //所有分片
        File[] files = sliceFile.listFiles();
        //按照1 2 3 4 排序，有序写入
        Arrays.sort(files, Comparator.comparing(o -> Integer.parseInt(o.getName())));
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
            e.printStackTrace();
        } finally {
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
     * 删除临时目录
     *
     * @param tempPath
     */
    public void deleteSliceTemp(String tempPath) {
        File file = new File(tempPath);
        if (file.exists()) {
            File[] files = file.listFiles();
            for (File cfile : files) {
                cfile.delete();
            }
            file.delete();
        }
    }


    /**
     * 直接的文件上传
     *
     * @param upFileDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    protected UploadBO easyUpload(UpFileDTO upFileDTO) {
        UploadBO uploadBO = new UploadBO();
        return uploadBO;
    }

    @Override
    protected DownloadFileVO downFile(FileFolderDO fileFolderDO) {
        FileInfoDO fileInfo = fileInfoDao.selectById(fileFolderDO.getFileId());

        //文件转换成数组byte
        byte[] bytes = FileUtil.FiletoByte(new File(fileInfo.getFilePath()));

        byte[] buffer = new byte[1024];
        BufferedInputStream bis = null;
        //输出流
        OutputStream os = null;
        try {
            //设置返回文件信息
            httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileInfo.getFileName(), "UTF-8"));
            httpServletResponse.setContentType("application/octet-stream");
            httpServletResponse.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            os = httpServletResponse.getOutputStream();
            bis = new BufferedInputStream(new ByteArrayInputStream(bytes));
            //写入文件
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关流了
            try {
                if (bis != null) {
                    bis.close();
                }
                if (os != null) {
                    os.flush();
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        DownloadFileVO downloadFileVO = new DownloadFileVO();
        downloadFileVO.setFilePath(fileInfo.getFilePath());
        downloadFileVO.setFileName(fileInfo.getFileName());
        return downloadFileVO;
    }

//    @Override
//    protected DownloadFileVO downFile(FileFolderDO fileFolderDO) {
//        FileInfoDO fileInfo = fileInfoDao.selectById(fileFolderDO.getFileId());
//
//        String fileName = null;
//        Long fileSize = fileInfo.getFileSize();
//        try {
//            fileName = URLEncoder.encode(fileInfo.getFileName(), "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//        }
//        //设置下载请求头
//        httpServletResponse.setContentType("application/x-download");
//        httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + fileName);
//
//        Long start = 0L;
//        Long end = fileSize;
//        OutputStream os = null;
//        try {
//            os = httpServletResponse.getOutputStream();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        //开启多线程下载
//        Long threadSize = fileSize / threadCount;
//        int currentCount = 0;
//        while (currentCount <= threadCount) {
//            start = threadSize * currentCount;
//            end = (currentCount + 1) * threadSize;
//            currentCount++;
//            //开启下载
//            new Thread(new DownloadThread(start, end, os, new File(fileInfo.getFilePath()))).run();
//        }
//        //让步下载线程
//        Thread.yield();
//        if (os != null) {
//            try {
//                os.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//
//        DownloadFileVO downloadFileVO = new DownloadFileVO();
//        downloadFileVO.setFilePath(fileInfo.getFilePath());
//        downloadFileVO.setFileName(fileInfo.getFileName());
//        return downloadFileVO;
//    }

    @Override
    protected boolean deleteFile(List<Integer> folderIds) {
        List<FileFolderDO> fileFolderDOS = fileFolderDao.selectByIds(folderIds);
        if (CollectionUtil.isEmpty(fileFolderDOS)
                || CollectionUtil.isEmpty(fileFolderDOS.stream().filter(t -> ObjectUtil.isNotNull(t.getFileId())).collect(Collectors.toList()))) {
            return false;
        }

        List<FileInfoDO> fileInfoDOS = fileInfoDao.selectByIds(fileFolderDOS.stream().filter(t -> ObjectUtil.isNotNull(t.getFileId()))
                .map(FileFolderDO::getFileId).collect(Collectors.toList()));
        if (CollectionUtil.isEmpty(fileInfoDOS)) {
            return false;
        }
        Map<String, FileInfoDO> fileMap = CollectionFunctionUtils.mapTo(fileInfoDOS, FileInfoDO::getFileId);
        List<FileFolderDO> folderInfile = fileFolderDao.selectByFileIds(fileInfoDOS.stream().map(FileInfoDO::getFileId).collect(Collectors.toList()));
        Map<String, List<FileFolderDO>> fileGroup = CollectionFunctionUtils.groupTo(folderInfile, FileFolderDO::getFileId);
        List<String> deleteFileIds = new ArrayList<>();
        fileGroup.forEach((key, value) -> {
            if (value.size() == 1) {
                FileInfoDO fileInfoDO = fileMap.get(key);
                deleteFile(fileInfoDO.getFilePath());
                /**
                 * 删除文件
                 */
                deleteFileIds.add(key);
            }
        });
        if (CollectionUtil.isNotEmpty(deleteFileIds)) {
            fileInfoDao.deleteByIds(deleteFileIds);
        }
        fileFolderDao.deleteByIds(folderIds);
        return true;
    }

    private boolean deleteFile(String path) {
        File file = new File(path);
        return file.delete();
    }

    /**
     * 请求上传id
     *
     * @param requestUpload
     * @return
     */
    @Override
    public UploadContext.Content requestUploadId(RequestUploadDTO requestUpload) {
        String prefix = "";
        if (ObjectUtil.isNotNull(requestUpload.getFolderId())) {
            //拼接前缀
            FileFolderDO fileFolderDO = fileFolderDao.selectById(requestUpload.getFolderId());
            prefix = fileHelpService.dfsGenerateFolderPrefix(fileFolderDO);
        }
//            String fileName = prefix + requestUpload.getFileName();
        String uploadId = UUID.randomUUID().toString();
        UploadContext.Content content = new UploadContext.Content();
        content.setPartETags(new HashMap<>());
        content.setFileKey(prefix);
        content.getParentIds().add(requestUpload.getFolderId());
        content.setUploadId(uploadId);
        UploadContext.setCache(uploadId, content);
        UploadContext.setId(requestUpload.getUniqueIdentifier(), uploadId);
        return content;
    }

    @Override
    public String accessFile(String fileId) {
//        return HttpUtil.createRequest(Method.GET,filePath).execute().body();
        FileInfoDO fileInfoDO = fileInfoDao.selectById(fileId);
        AssertUtil.isFalse(ObjectUtil.isNull(fileInfoDO), ResponseCode.FILE_NOT_EXIST);
        File file = new File(fileInfoDO.getFilePath());
        if (file.exists()) {
            StringBuilder content = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(fileInfoDO.getFilePath()), 8192)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                    content.append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return content.toString();
        }
        return null;
    }
}
