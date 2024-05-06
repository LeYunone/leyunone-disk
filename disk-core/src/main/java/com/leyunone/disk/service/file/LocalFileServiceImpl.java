package com.leyunone.disk.service.file;

import com.leyunone.disk.dao.entry.FileInfoDO;
import com.leyunone.disk.dao.repository.FileFolderDao;
import com.leyunone.disk.dao.repository.FileInfoDao;
import com.leyunone.disk.model.bo.UploadBO;
import com.leyunone.disk.model.dto.FileFolderDTO;
import com.leyunone.disk.model.dto.RequestUploadDTO;
import com.leyunone.disk.model.dto.UpFileDTO;
import com.leyunone.disk.model.vo.DownloadFileVO;
import com.leyunone.disk.service.FileService;
import org.springframework.stereotype.Service;

/**
 * :)
 *
 * @Author LeYunone
 * @Date 2024/4/22 17:42
 */
public class LocalFileServiceImpl extends AbstractFileService {

    public LocalFileServiceImpl(FileInfoDao fileInfoDao, FileFolderDao fileFolderDao) {
        super(fileInfoDao, fileFolderDao);
    }

    @Override
    protected void deleteFile(String fileId) {
        
    }

    @Override
    protected UploadBO uploadFile(UpFileDTO upFileDTO) {
        return null;
    }

    @Override
    protected DownloadFileVO downFile(FileInfoDO fileInfo) {
        return null;
    }


    @Override
    public String requestUpload(RequestUploadDTO requestUpload) {
        return null;
    }
}
