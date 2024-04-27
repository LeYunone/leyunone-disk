package com.leyunone.disk.system.config;

import com.leyunone.disk.dao.repository.FileFolderDao;
import com.leyunone.disk.dao.repository.FileInfoDao;
import com.leyunone.disk.service.FileService;
import com.leyunone.disk.service.file.AliOssFileService;
import com.leyunone.disk.service.file.LocalFileServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * :)
 *
 * @Author LeYunone
 * @Date 2024/4/24 17:54
 */
@Configuration
public class FileServiceLoadConfig {

    @Value("${disk.type:oss}")
    private String type;

    @Bean
    public FileService fileService(FileInfoDao fileInfoDao, FileFolderDao fileFolderDao) {
        switch (type) {
            case "oss":
                return new AliOssFileService(fileInfoDao, fileFolderDao);
            case "local":
                return new LocalFileServiceImpl(fileInfoDao, fileFolderDao);
        }
        return new AliOssFileService(fileInfoDao, fileFolderDao);
    }
}
