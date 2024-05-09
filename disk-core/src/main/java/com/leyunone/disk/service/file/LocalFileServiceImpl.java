package com.leyunone.disk.service.file;

import com.leyunone.disk.dao.entry.FileFolderDO;
import com.leyunone.disk.dao.repository.FileFolderDao;
import com.leyunone.disk.dao.repository.FileInfoDao;
import com.leyunone.disk.model.bo.UploadBO;
import com.leyunone.disk.model.dto.RequestUploadDTO;
import com.leyunone.disk.model.dto.UpFileDTO;
import com.leyunone.disk.model.vo.DownloadFileVO;

import java.util.List;

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
    protected boolean deleteFile(List<Integer> folderIds) {
        return false;
    }

    @Override
    protected UploadBO shardUpload(UpFileDTO upFileDTO) {
        return null;
    }

    @Override
    protected UploadBO easyUpload(UpFileDTO upFileDTO) {
        return null;
    }

    @Override
    protected DownloadFileVO downFile(FileFolderDO fileFolderDO) {
        return null;
    }

    @Override
    public String requestUpload(RequestUploadDTO requestUpload) {
        return null;
    }
}
