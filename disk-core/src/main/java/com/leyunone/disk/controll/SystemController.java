package com.leyunone.disk.controll;

import com.leyunone.disk.model.DataResponse;
import com.leyunone.disk.model.vo.SelectTreeVO;
import com.leyunone.disk.service.FileQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * :)
 *
 * @Author LeYunone
 * @Date 2024/4/25 15:58
 */
@RestController
@RequestMapping("/system")
public class SystemController {

    @Autowired
    private FileQueryService fileQueryService;

    @GetMapping("/getEnvironment")
    public DataResponse<String> getEnvironment() {
        return DataResponse.of("oss");
    }

    @GetMapping("/folderTree")
    public DataResponse<List<SelectTreeVO>> getFolderTree() {
        List<SelectTreeVO> folderTree = fileQueryService.getFolderTree();
        return DataResponse.of(folderTree);
    }
}
