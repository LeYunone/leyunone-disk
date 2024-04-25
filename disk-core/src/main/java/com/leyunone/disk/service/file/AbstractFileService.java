package com.leyunone.disk.service.file;

import cn.hutool.core.util.ObjectUtil;
import com.leyunone.disk.dao.entry.FileInfoDO;
import com.leyunone.disk.dao.repository.FileFolderDao;
import com.leyunone.disk.dao.repository.FileInfoDao;
import com.leyunone.disk.model.ResponseCode;
import com.leyunone.disk.model.bo.UploadBO;
import com.leyunone.disk.model.dto.FileFolderDTO;
import com.leyunone.disk.model.dto.UpFileDTO;
import com.leyunone.disk.service.FileService;
import com.leyunone.disk.util.AssertUtil;

import java.util.UUID;

/**
 * :)
 *
 * @Author pengli
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
    public void upload(UpFileDTO upFileDTO) {
        UploadBO uploadBO = this.uploadFile(upFileDTO);
        if (uploadBO.isSuccess()) {
            FileInfoDO fileInfoDO = new FileInfoDO();
            fileInfoDO.setFileId(UUID.randomUUID().toString());
            fileInfoDO.setFileName(uploadBO.getFileName());
            fileInfoDO.setFileSize(upFileDTO.getTotalSize());
            fileInfoDO.setFileType(upFileDTO.getFileType());
            fileInfoDO.setFilePath(uploadBO.getFilePath());
        }
    }

    @Override
    public String down(String fileId) {
        FileInfoDO fileInfoDO = fileInfoDao.selectById(fileId);
        AssertUtil.isFalse(ObjectUtil.isNull(fileInfoDO), ResponseCode.FILE_NOT_EXIST);
        return this.downFile(fileInfoDO);
    }

    @Override
    public void delete(String fileId) {
        fileFolderDao.deleteByFileId(fileId);
        fileInfoDao.deleteById(fileId);
        this.deleteFile(fileId);
    }

    protected abstract void deleteFile(String fileId);

    protected abstract UploadBO uploadFile(UpFileDTO upFileDTO);

    protected abstract String downFile(FileInfoDO fileInfo);

    @Override
    public void createFolder(FileFolderDTO fileFolderDTO) {

    }
}
