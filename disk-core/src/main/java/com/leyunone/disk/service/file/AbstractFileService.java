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
import org.springframework.transaction.annotation.Transactional;

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
        UploadBO uploadBO = null;
        if (upFileDTO.isEasyUpload()) {
            uploadBO = this.easyUpload(upFileDTO);
        } else {
            uploadBO = this.shardUpload(upFileDTO);
        }
        if (uploadBO.isSuccess()) {
            String fileId = uploadBO.getFileId();
            if (!uploadBO.isNoNewFile()) {
                FileInfoDO fileInfoDO = new FileInfoDO();
                fileInfoDO.setFileId(UUID.randomUUID().toString());
                fileInfoDO.setFileName(uploadBO.getFileName());
                fileInfoDO.setFileSize(uploadBO.getTotalSize());
                fileInfoDO.setFileType(FileTypeEnum.loadType(uploadBO.getFileType()));
                fileInfoDO.setFilePath(uploadBO.getFilePath());
                fileInfoDO.setFileMd5(uploadBO.getIdentifier());
                fileInfoDao.save(fileInfoDO);
                fileId = fileInfoDO.getFileId();
            }
            FileFolderDO fileFolderDO = new FileFolderDO();
            fileFolderDO.setFolder(false);
            fileFolderDO.setParentId(upFileDTO.getParentId());
            fileFolderDO.setFileId(fileId);
            fileFolderDao.save(fileFolderDO);
        }
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
    public void delete(FileDTO fileDTO) {
        if (!fileDTO.isFolder()) {
            boolean delete = this.deleteFile(fileDTO.getFolderId());
            if (delete) {
                FileInfoDO fileInfoDO = fileInfoDao.selectById(fileDTO.getFileId());
                AssertUtil.isFalse(ObjectUtil.isNull(fileInfoDO), ResponseCode.FILE_NOT_EXIST);
                fileInfoDao.deleteById(fileDTO.getFileId());
                fileFolderDao.deleteByFileId(fileDTO.getFileId());
            }
        } else {
            fileFolderDao.deleteById(fileDTO.getFolderId());
        }
    }

    protected abstract boolean deleteFile(Integer folderId);
    //分片上传
    protected abstract UploadBO shardUpload(UpFileDTO upFileDTO);
    //简单上传
    protected abstract UploadBO easyUpload(UpFileDTO upFileDTO);

    protected abstract DownloadFileVO downFile(FileFolderDO fileFolderDO);

    @Override
    public void createFolder(FileFolderDTO fileFolderDTO) {
        FileFolderDO fileFolderDO = new FileFolderDO();
        fileFolderDO.setFolderName(fileFolderDTO.getNewFolderName() + "/");
        fileFolderDO.setFolder(true);
        fileFolderDO.setParentId(fileFolderDTO.getParentId());
        fileFolderDao.save(fileFolderDO);
    }
}
