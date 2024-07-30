package com.leyunone.disk.service.file;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.aliyun.oss.model.PartETag;
import com.leyunone.disk.common.UploadContext;
import com.leyunone.disk.common.enums.FileTypeEnum;
import com.leyunone.disk.dao.entry.FileFolderDO;
import com.leyunone.disk.dao.entry.FileInfoDO;
import com.leyunone.disk.dao.repository.FileFolderDao;
import com.leyunone.disk.dao.repository.FileHistoryDao;
import com.leyunone.disk.dao.repository.FileInfoDao;
import com.leyunone.disk.manager.OssManager;
import com.leyunone.disk.model.ResponseCode;
import com.leyunone.disk.model.bo.UploadBO;
import com.leyunone.disk.model.dto.RequestUploadDTO;
import com.leyunone.disk.model.dto.UpFileDTO;
import com.leyunone.disk.model.vo.DownloadFileVO;
import com.leyunone.disk.service.FileHelpService;
import com.leyunone.disk.service.FileHistoryService;
import com.leyunone.disk.util.AssertUtil;
import com.leyunone.disk.util.CollectionFunctionUtils;
import com.leyunone.disk.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * :)
 * 阿里云oss服务
 *
 * @Author LeYunone
 * @Date 2024/4/22 17:42
 */
public class AliOssFileServiceImpl extends AbstractFileService {

    @Autowired
    private OssManager ossManager;
    @Autowired
    private FileHelpService fileHelpService;

    public AliOssFileServiceImpl(FileInfoDao fileInfoDao, FileFolderDao fileFolderDao, FileHistoryService fileHistoryService) {
        super(fileInfoDao, fileFolderDao, fileHistoryService);
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
        int currentChunkNo = upFileDTO.getChunkNumber();
        int totalChunks = upFileDTO.getTotalChunks();
        String ossSlicesId = upFileDTO.getUploadId();
        //字节流转换
        Map<Integer, PartETag> partETags = content.getPartETags();
        boolean merge = false;
        //分片上传
        if (!partETags.containsKey(currentChunkNo)) {
            //每次上传分片之后，OSS的返回结果会包含一个PartETag
            PartETag partETag = null;
            try {
                partETag = ossManager.partUploadFile(content.getFileKey(), file.getInputStream(), ossSlicesId,
                        upFileDTO.getIdentifier(), currentChunkNo, file.getSize());
            } catch (IOException e) {
                e.printStackTrace();
            }
            partETags.put(currentChunkNo, partETag);
            //如果是最后一个子分片，将合并线程放开
            if (currentChunkNo != totalChunks && partETags.size() == totalChunks) {
                merge = true;
            }
        }
        //分片编号等于总片数的时候合并文件,如果符合条件则合并文件，否则继续等待
        if (currentChunkNo == totalChunks) {
            //合并文件
            if (partETags.size() != totalChunks) {
                //挂起 等待小分片上传完毕
            } else {
                merge = true;
            }
        }
        content.setPartETags(partETags);
        UploadContext.setCache(ossSlicesId, content);
        if (merge) {
            //合并文件，注意：partETags必须是所有分片的所以必须存入redis，然后取出放入集合
            try {
                String url = ossManager.completePartUploadFile(content.getFileKey(), ossSlicesId, new ArrayList<>(partETags.values()));
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
            }
        }
        return uploadBO;
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
        AssertUtil.isFalse(ObjectUtil.isNull(upFileDTO.getFile()), "file is empty");
        MultipartFile file = upFileDTO.getFile();
        /**
         * 日期文件前缀 yyyy-MM-dd
         */
        if (upFileDTO.isHasDate()) {
            FileFolderDO fileFolderDO = fileFolderDao.selectByNameAndParentId(DateUtil.today() + "/", upFileDTO.getParentId());
            if (ObjectUtil.isNull(fileFolderDO)) {
                fileFolderDO = new FileFolderDO();
                fileFolderDO.setFolderName(DateUtil.today() + "/");
                fileFolderDO.setParentId(upFileDTO.getParentId());
                fileFolderDO.setFolder(true);
                fileFolderDao.save(fileFolderDO);
            }
            upFileDTO.setParentId(fileFolderDO.getFolderId());
        }

        try {
            String md5 = MD5Util.fileToMD5(upFileDTO.getFile().getBytes());
            uploadBO.setIdentifier(md5);
            FileInfoDO repeatFile = fileHelpService.repeatFile(md5, upFileDTO.getParentId());
            if (ObjectUtil.isNull(repeatFile)) {
                FileFolderDO fileFolderDO = fileFolderDao.selectById(upFileDTO.getParentId());
                String prefix = fileHelpService.dfsGenerateFolderPrefix(fileFolderDO);
                String url = ossManager.uploadFile(prefix + file.getOriginalFilename(), file.getInputStream());
                uploadBO.setFileName(file.getOriginalFilename());
                uploadBO.setFilePath(url);
                uploadBO.setTotalSize(file.getSize());
                uploadBO.setSuccess(true);
            }
        } catch (IOException e) {
            e.printStackTrace();
            uploadBO.setSuccess(false);
        }

        uploadBO.setParentId(upFileDTO.getParentId());
        return uploadBO;
    }

    @Override
    protected DownloadFileVO downFile(FileFolderDO fileFolderDO) {
        String prefix = fileHelpService.dfsGenerateFolderPrefix(fileFolderDO);
        FileInfoDO fileInfo = fileInfoDao.selectById(fileFolderDO.getFileId());
        DownloadFileVO downloadFileVO = new DownloadFileVO();
        /**
         * 图片永久
         */
        Long time = null;
        switch (FileTypeEnum.load(fileInfo.getFileType())) {
            case FILE_IMG:
                break;
            default:
                ;
        }
        downloadFileVO.setFilePath(ossManager.getFileUrl(prefix + fileInfo.getFileName(), time));
        downloadFileVO.setFileName(fileInfo.getFileName());
        return downloadFileVO;
    }

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
        fileGroup.entrySet().forEach(ety -> {
            if (ety.getValue().size() == 1) {
                String prefix = fileHelpService.dfsGenerateFolderPrefix(ety.getValue().get(0));
                ossManager.deleteFile(prefix + fileMap.get(ety.getKey()).getFileName());
                deleteFileIds.add(ety.getKey());
            }
        });
        if (CollectionUtil.isNotEmpty(deleteFileIds)) {
            fileInfoDao.deleteByIds(deleteFileIds);
        }
        fileFolderDao.deleteByIds(folderIds);
        return true;
    }

    /**
     * 请求oss分片上传id
     *
     * @param requestUpload
     * @return
     */
    @Override
    protected UploadContext.Content requestUploadId(RequestUploadDTO requestUpload) {
        String prefix = "";
        if (ObjectUtil.isNotNull(requestUpload.getFolderId())) {
            //拼接前缀
            FileFolderDO fileFolderDO = fileFolderDao.selectById(requestUpload.getFolderId());
            prefix = fileHelpService.dfsGenerateFolderPrefix(fileFolderDO);
        }
        String fileName = prefix + requestUpload.getFileName();
        String uploadId = ossManager.getUploadId(fileName);
        UploadContext.Content content = new UploadContext.Content();
        content.setPartETags(new HashMap<>());
        content.setFileKey(fileName);
        content.setUploadId(uploadId);
        content.getParentIds().add(requestUpload.getFolderId());
        UploadContext.setCache(uploadId, content);
        UploadContext.setId(requestUpload.getUniqueIdentifier(), uploadId);
        return content;
    }

    @Override
    public String accessFile(String fileId) {
        FileInfoDO fileInfoDO = fileInfoDao.selectById(fileId);
        AssertUtil.isFalse(ObjectUtil.isNull(fileInfoDO), ResponseCode.FILE_NOT_EXIST);
        //http请求拿到文件字符内容
        String body = HttpUtil.createRequest(Method.GET, fileInfoDO.getFilePath()).execute().body();
        return body;
    }
}
