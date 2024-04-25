package com.leyunone.disk.service;

import com.leyunone.disk.model.dto.FileFolderDTO;
import com.leyunone.disk.model.dto.UpFileDTO;

/**
 * :)
 *
 * @Author pengli
 * @Date 2024/4/22 17:33
 */
public interface FileService {

    void upload(UpFileDTO upFileDTO);

    String down(String fileId);

    void delete(String fileId);

    void createFolder(FileFolderDTO fileFolderDTO);
    
    String requestUpload(String fileName);
}
