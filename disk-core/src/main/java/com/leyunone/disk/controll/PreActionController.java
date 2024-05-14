package com.leyunone.disk.controll;

import cn.hutool.core.util.ObjectUtil;
import com.leyunone.disk.model.DataResponse;
import com.leyunone.disk.model.dto.RequestUploadDTO;
import com.leyunone.disk.model.dto.UpFileDTO;
import com.leyunone.disk.model.vo.FileValidatorVO;
import com.leyunone.disk.service.UploadPreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * :)
 *
 * @Author LeYunone
 * @Date 2024/4/23 14:26
 */
@RestController
@RequestMapping("/pre")
public class PreActionController {

    @Autowired
    private UploadPreService uploadPreService;

    @PostMapping("/requestUploadFile")
    public DataResponse<FileValidatorVO> requestUploadFile(@RequestBody RequestUploadDTO uploadDTO) {
        if (ObjectUtil.isNull(uploadDTO.getFolderId())) {
            uploadDTO.setFolderId(-1);
        }
        FileValidatorVO fileValidatorVO = uploadPreService.judgeFile(uploadDTO);
        return DataResponse.of(fileValidatorVO);
    }
}
