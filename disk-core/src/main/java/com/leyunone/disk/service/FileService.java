package com.leyunone.disk.service;

import com.leyunone.disk.model.bo.UploadBO;
import com.leyunone.disk.model.dto.FileDTO;
import com.leyunone.disk.model.dto.FileFolderDTO;
import com.leyunone.disk.model.dto.RequestUploadDTO;
import com.leyunone.disk.model.dto.UpFileDTO;
import com.leyunone.disk.model.vo.DownloadFileVO;

import java.util.List;

/**
 * :)
 *
 * @Author LeYunone
 * @Date 2024/4/22 17:33
 */
public interface FileService {

    UploadBO upload(UpFileDTO upFileDTO);

    DownloadFileVO down(Integer folderId);

    void delete(List<FileDTO> fileDTO);
    
    String requestUpload(RequestUploadDTO requestUpload);
    
    void cancelUpload(UpFileDTO upFileDTO);

    String accessFile(String fileId);
}
