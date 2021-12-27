package com.leyuna.disk.controll;

import com.leyuna.disk.DataResponse;
import com.leyuna.disk.co.FileInfoCO;
import com.leyuna.disk.dto.file.FileDTO;
import com.leyuna.disk.dto.file.UpFileDTO;
import com.leyuna.disk.service.file.FileQueryService;
import com.leyuna.disk.service.file.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @Autowired
    private FileService fileService;
    /**
     * 查询服务器内文件[条件-分页]
     * @return
     */
    @GetMapping("/selectFile")
    public DataResponse selectFileList(FileDTO file){
        DataResponse<List<FileInfoCO>> listDataResponse = fileQueryService.selectFile(file);
        return listDataResponse;
    }

    /**
     * 请求存储文件 >存储文件的前提
     * @return
     */
    @RequestMapping("/requestSaveFile")
    public DataResponse requestSaveFile(UpFileDTO upFileDTO){
        return fileService.JudgeFile(upFileDTO);
    }

    /**
     * 存储文件
     */
    @PostMapping("/saveFile")
    public DataResponse saveFile(@RequestBody UpFileDTO upFileDTO){
        return fileService.savaFile(upFileDTO);
    }

    /**
     * 删除指定文件
     * @param id
     * @return
     */
    @PostMapping("/deleteFile")
    public DataResponse deleteFile(String id){
        return fileService.deleteFile(id);
    }
}
