package com.leyunone.disk.service.file;

import cn.hutool.core.net.multipart.UploadFile;
import com.leyunone.disk.dao.entry.FileInfoDO;
import com.leyunone.disk.dao.repository.FileFolderDao;
import com.leyunone.disk.dao.repository.FileInfoDao;
import com.leyunone.disk.model.bo.UploadBO;
import com.leyunone.disk.model.dto.FileFolderDTO;
import com.leyunone.disk.model.dto.UpFileDTO;
import com.leyunone.disk.service.FileService;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * :)
 *
 * @Author pengli
 * @Date 2024/4/22 17:26
 */
@Service
public abstract class FileServiceImpl implements FileService {

    protected FileInfoDao fileInfoDao;
    protected FileFolderDao fileFolderDao;

    public FileServiceImpl(FileInfoDao fileInfoDao, FileFolderDao fileFolderDao) {
        this.fileInfoDao = fileInfoDao;
        this.fileFolderDao = fileFolderDao;
    }

    @Override
    public void upload(UpFileDTO upFileDTO) {
        UploadBO uploadBO = uploadFile(upFileDTO);

        if (uploadBO.isSuccess()) {
            FileInfoDO fileInfoDO = new FileInfoDO();
            fileInfoDO.setFileId(UUID.randomUUID().toString());
            fileInfoDO.setFileName(uploadBO.getFileName());
            fileInfoDO.setFileSize(upFileDTO.getTotalSize());
            fileInfoDO.setFileType(upFileDTO.getFileType());
            fileInfoDO.setFilePath(uploadBO.getFilePath());
        }
    }

    public abstract UploadBO uploadFile(UpFileDTO upFileDTO);

    @Override
    public void down(Integer fileId) {

    }

    @Override
    public void delete(Integer fileId) {

    }

    @Override
    public void createFolder(FileFolderDTO fileFolderDTO) {

    }
}
