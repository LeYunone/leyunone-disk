package com.leyunone.disk.service;

import com.leyunone.disk.model.dto.UpFileDTO;
import com.leyunone.disk.model.vo.FileValidatorVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * :)
 *
 * @Author LeYunone
 * @Date 2024/4/22 17:26
 */
public interface UploadPreService {

    FileValidatorVO judgeFile(String md5);

    void checkFile(UpFileDTO upFileDTO);
}
