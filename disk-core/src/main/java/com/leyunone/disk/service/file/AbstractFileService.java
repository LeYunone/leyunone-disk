package com.leyunone.disk.service.file;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.leyunone.disk.common.UploadContext;
import com.leyunone.disk.common.enums.FileTypeEnum;
import com.leyunone.disk.dao.entry.FileFolderDO;
import com.leyunone.disk.dao.entry.FileInfoDO;
import com.leyunone.disk.dao.repository.FileExtendContentDao;
import com.leyunone.disk.dao.repository.FileFolderDao;
import com.leyunone.disk.dao.repository.FileInfoDao;
import com.leyunone.disk.model.ResponseCode;
import com.leyunone.disk.model.bo.UploadBO;
import com.leyunone.disk.model.dto.FileDTO;
import com.leyunone.disk.model.dto.FileFolderDTO;
import com.leyunone.disk.model.dto.RequestUploadDTO;
import com.leyunone.disk.model.dto.UpFileDTO;
import com.leyunone.disk.model.vo.DownloadFileVO;
import com.leyunone.disk.service.FileContentService;
import com.leyunone.disk.service.FileService;
import com.leyunone.disk.util.AssertUtil;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * :)
 *
 * @Author LeYunone
 * @Date 2024/4/22 17:26
 */
public abstract class AbstractFileService implements FileService {

    protected FileInfoDao fileInfoDao;
    protected FileFolderDao fileFolderDao;

    public AbstractFileService(FileInfoDao fileInfoDao, FileFolderDao fileFolderDao) {
        this.fileInfoDao = fileInfoDao;
        this.fileFolderDao = fileFolderDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UploadBO upload(UpFileDTO upFileDTO) {
        UploadBO uploadBO = null;
        if (upFileDTO.isEasyUpload()) {
            uploadBO = this.easyUpload(upFileDTO);
        } else {
            uploadBO = this.shardUpload(upFileDTO);
        }
        if (uploadBO.isSuccess()) {
            FileInfoDO fileInfoDO = new FileInfoDO();
            fileInfoDO.setFileId(UUID.randomUUID().toString());
            fileInfoDO.setFileName(uploadBO.getFileName());
            fileInfoDO.setFileSize(uploadBO.getTotalSize());
            fileInfoDO.setFileType(FileTypeEnum.loadType(upFileDTO.getFileType().toUpperCase()).getValue());
            fileInfoDO.setFilePath(uploadBO.getFilePath());
            fileInfoDO.setFileMd5(uploadBO.getIdentifier());
            fileInfoDao.save(fileInfoDO);
            FileFolderDO fileFolderDO = new FileFolderDO();
            fileFolderDO.setFolder(false);
            fileFolderDO.setParentId(uploadBO.getParentId());
            fileFolderDO.setFileId(fileInfoDO.getFileId());
            fileFolderDao.save(fileFolderDO);
            uploadBO.setFileId(fileInfoDO.getFileId());
//            fileContentService.saveFileContent(uploadBO);
        }
        return uploadBO;
    }

    @Override
    public DownloadFileVO down(Integer folderId) {
        FileFolderDO fileFolderDO = fileFolderDao.selectById(folderId);
        AssertUtil.isFalse(ObjectUtil.isNull(fileFolderDO) || ObjectUtil.isNull(fileFolderDO.getFileId()), ResponseCode.FILE_NOT_EXIST);
        FileInfoDO fileInfoDO = fileInfoDao.selectById(fileFolderDO.getFileId());
        AssertUtil.isFalse(ObjectUtil.isNull(fileInfoDO), ResponseCode.FILE_NOT_EXIST);
        return this.downFile(fileFolderDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<FileDTO> fileFolders) {
        List<FileDTO> folders = fileFolders.stream().filter(FileDTO::isFolder).collect(Collectors.toList());
        List<FileDTO> files = fileFolders.stream().filter(t -> !t.isFolder()).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(folders)) {
            fileFolderDao.deleteByIds(folders.stream().map(FileDTO::getFolderId).collect(Collectors.toList()));
        }
        if (CollectionUtil.isNotEmpty(files)) {
            this.deleteFile(files.stream().map(FileDTO::getFolderId).collect(Collectors.toList()));
        }
    }

    @Override
    public String requestUpload(RequestUploadDTO requestUpload) {
        String uploadId = UploadContext.getId(requestUpload.getUniqueIdentifier());
        if (StringUtils.isNotBlank(uploadId)) {
            //该文件已经在上传流程中
            UploadContext.Content upload = UploadContext.getUpload(uploadId);
            if (ObjectUtil.isNotNull(upload)) {
                upload.getParentIds().add(requestUpload.getFolderId());
            }
        } else {
            UploadContext.Content content = this.requestUploadId(requestUpload);
            uploadId = content.getUploadId();
            //埋点记录文件

        }
        return uploadId;
    }

    protected abstract UploadContext.Content requestUploadId(RequestUploadDTO requestUpload);

    protected abstract boolean deleteFile(List<Integer> folderIds);

    //分片上传
    protected abstract UploadBO shardUpload(UpFileDTO upFileDTO);

    //简单上传
    protected abstract UploadBO easyUpload(UpFileDTO upFileDTO);

    protected abstract DownloadFileVO downFile(FileFolderDO fileFolderDO);

    @Override
    public void cancelUpload(UpFileDTO upFileDTO) {
        String uploadId = upFileDTO.getUploadId();
        UploadContext.Content upload = UploadContext.getUpload(uploadId);
        if (ObjectUtil.isNotNull(upload)) {
            if (upload.getParentIds().size() == 1) {
                //删除
                UploadContext.removeCache(uploadId);
                UploadContext.removeId(upFileDTO.getUniqueIdentifier());
            }
        }
    }
}
