package com.leyuna.disk.web.controll;

import com.leyuna.disk.client.DataResponse;
import com.leyuna.disk.client.dto.file.FileDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author pengli
 * @create 2021-12-09 10:38
 * 文件方接口
 */
@RestController
@RequestMapping("/file")
public class FileController {

    /**
     * 查询服务器内文件[条件-分页]
     * @return
     */
    @RequestMapping("/selectFile")
    public DataResponse selectFileList(FileDTO fileDTO){
        
    }
}
