package com.leyunone.disk.service.file;

import cn.hutool.core.util.ObjectUtil;
import com.leyunone.disk.common.enums.FileTypeEnum;
import com.leyunone.disk.dao.entry.FileFolderDO;
import com.leyunone.disk.dao.entry.FileInfoDO;
import com.leyunone.disk.dao.repository.FileFolderDao;
import com.leyunone.disk.dao.repository.FileInfoDao;
import com.leyunone.disk.model.ResponseCode;
import com.leyunone.disk.model.bo.UploadBO;
import com.leyunone.disk.model.dto.FileDTO;
import com.leyunone.disk.model.dto.FileFolderDTO;
import com.leyunone.disk.model.dto.UpFileDTO;
import com.leyunone.disk.model.vo.DownloadFileVO;
import com.leyunone.disk.service.FileService;
import com.leyunone.disk.util.AssertUtil;

import java.util.UUID;

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
    public void upload(UpFileDTO upFileDTO) {
        UploadBO uploadBO = this.uploadFile(upFileDTO);
        if (uploadBO.isSuccess()) {
            FileInfoDO fileInfoDO = new FileInfoDO();
            fileInfoDO.setFileId(UUID.randomUUID().toString());
            fileInfoDO.setFileName(uploadBO.getFileName());
            fileInfoDO.setFileSize(upFileDTO.getTotalSize());
            fileInfoDO.setFileType(FileTypeEnum.loadType(upFileDTO.getFileType()));
            fileInfoDO.setFilePath(uploadBO.getFilePath());
            fileInfoDO.setFileMd5(upFileDTO.getIdentifier());
            fileInfoDao.save(fileInfoDO);
            FileFolderDO fileFolderDO = new FileFolderDO();
            fileFolderDO.setFolder(false);
            fileFolderDO.setParentId(upFileDTO.getFileFolderId());
            fileFolderDO.setFileId(fileInfoDO.getFileId());
            fileFolderDao.save(fileFolderDO);
        }
    }

    @Override
    public DownloadFileVO down(String fileId) {
        FileInfoDO fileInfoDO = fileInfoDao.selectById(fileId);
        AssertUtil.isFalse(ObjectUtil.isNull(fileInfoDO), ResponseCode.FILE_NOT_EXIST);
        return this.downFile(fileInfoDO);
    }

    @Override
    public void delete(FileDTO fileDTO) {
        if (!fileDTO.isFolder()) {
            this.deleteFile(fileDTO.getFileId());
            FileInfoDO fileInfoDO = fileInfoDao.selectById(fileDTO.getFileId());
            AssertUtil.isFalse(ObjectUtil.isNull(fileInfoDO), ResponseCode.FILE_NOT_EXIST);
            fileInfoDao.deleteById(fileDTO.getFileId());
            fileFolderDao.deleteByFileId(fileDTO.getFileId());
        }else{
            fileFolderDao.deleteById(fileDTO.getFolderId());
        }
    }

    protected abstract void deleteFile(String fileId);

    protected abstract UploadBO uploadFile(UpFileDTO upFileDTO);

    protected abstract DownloadFileVO downFile(FileInfoDO fileInfo);

    @Override
    public void createFolder(FileFolderDTO fileFolderDTO) {
        FileFolderDO fileFolderDO = new FileFolderDO();
        fileFolderDO.setFolderName(fileFolderDTO.getNewFolderName() + "/");
        fileFolderDO.setFolder(true);
        fileFolderDO.setParentId(fileFolderDTO.getParentId());
        fileFolderDao.save(fileFolderDO);
    }
}
