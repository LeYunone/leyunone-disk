package com.leyunone.disk.service.file;

import com.leyunone.disk.dao.repository.FileFolderDao;
import com.leyunone.disk.dao.repository.FileInfoDao;
import com.leyunone.disk.model.bo.UploadBO;
import com.leyunone.disk.model.dto.UpFileDTO;
import org.springframework.stereotype.Service;

/**
 * :)
 * 阿里云oss服务
 *
 * @Author LeYunone
 * @Date 2024/4/22 17:42
 */
@Service
public class AliOssFileServiceImpl extends FileServiceImpl {
    
    public AliOssFileServiceImpl(FileInfoDao fileInfoDao, FileFolderDao fileFolderDao) {
        super(fileInfoDao, fileFolderDao);
    }

    @Override
    public UploadBO uploadFile(UpFileDTO upFileDTO) {
        
        return null;
    }
}
