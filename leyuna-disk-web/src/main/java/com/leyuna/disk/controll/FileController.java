package com.leyuna.disk.controll;

import com.leyuna.disk.DataResponse;
import com.leyuna.disk.co.FileInfoCO;
import com.leyuna.disk.dto.file.FileDTO;
import com.leyuna.disk.service.file.FileQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author pengli
 * @create 2021-12-09 10:38
 * 文件方接口
 */
@RestController
@RequestMapping("file")
public class FileController {

    @Autowired
    private FileQueryService fileQueryService;
    /**
     * 查询服务器内文件[条件-分页]
     * @return
     */
    @GetMapping("/selectFile/{id}")
    public DataResponse selectFileList(@PathVariable("id") String id){
        DataResponse<List<FileInfoCO>> listDataResponse = fileQueryService.selectFile(id);
        return listDataResponse;
    }
}
