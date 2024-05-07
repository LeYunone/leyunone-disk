package com.leyunone.disk.service;

import com.leyunone.disk.model.dto.FileDTO;
import com.leyunone.disk.model.dto.FileFolderDTO;
import com.leyunone.disk.model.dto.RequestUploadDTO;
import com.leyunone.disk.model.dto.UpFileDTO;
import com.leyunone.disk.model.vo.DownloadFileVO;

/**
 * :)
 *
 * @Author LeYunone
 * @Date 2024/4/22 17:33
 */
public interface FileService {

    void upload(UpFileDTO upFileDTO);

    DownloadFileVO down(Integer folderId);

    void delete(FileDTO fileDTO);

    void createFolder(FileFolderDTO fileFolderDTO);
    
    String requestUpload(RequestUploadDTO requestUpload);
}
