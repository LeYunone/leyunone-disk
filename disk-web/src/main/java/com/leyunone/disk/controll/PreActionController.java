package com.leyunone.disk.controll;

import com.leyunone.disk.model.DataResponse;
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
 * @Author pengli
 * @Date 2024/4/23 14:26
 */
@RestController
@RequestMapping("/pre")
public class PreActionController {

    @Autowired
    private UploadPreService uploadPreService;

    @PostMapping("/requestUploadFile")
    public DataResponse<FileValidatorVO> requestUploadFile(@RequestBody String uniqueIdentifier) {
        FileValidatorVO fileValidatorVO = uploadPreService.judgeFile(uniqueIdentifier);
        return DataResponse.of(fileValidatorVO);
    }
}
