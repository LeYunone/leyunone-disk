package com.leyuna.disk.controll;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leyuna.disk.DataResponse;
import com.leyuna.disk.co.FileInfoCO;
import com.leyuna.disk.dto.file.FileDTO;
import com.leyuna.disk.dto.file.UpFileDTO;
import com.leyuna.disk.service.file.FileQueryService;
import com.leyuna.disk.service.file.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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
    @PostMapping("/selectFile")
    public DataResponse<Page<FileInfoCO>> selectFileList(@RequestBody FileDTO file){
        DataResponse<Page<FileInfoCO>> listDataResponse = fileQueryService.selectFile(file);
        return listDataResponse;
    }

    @GetMapping("/selectAllFileSize")
    public DataResponse<Double> selectAllFileSize(String userId){
        return fileQueryService.selectAllFileSizeByUserId(userId);
    }

    /**
     * 请求存储文件 >存储文件的前提
     * @return
     */
    @PostMapping("/requestSaveFile")
    public DataResponse<Integer> requestSaveFile(@RequestParam("userId") String userId,
                                                             @RequestPart MultipartFile file){
        UpFileDTO upFileDTO=new UpFileDTO();
        upFileDTO.setUserId(userId);
        upFileDTO.setFile(file);
        return fileService.judgeFile(upFileDTO);
    }

    /**
     * 存储文件
     */
    @PostMapping("/saveFile")
    public DataResponse saveFile(@RequestParam("userId") String userId,
                                 @RequestPart MultipartFile file,
                                 @RequestParam(value = "saveTime", required = false) String saveTime){
        UpFileDTO upFileDTO=new UpFileDTO();
        upFileDTO.setUserId(userId);
        upFileDTO.setFile(file);
        upFileDTO.setSaveTime(saveTime);
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

    /**
     * 下载文件
     * @param id
     * @return
     */
    @PostMapping("/downloadFile")
    public DataResponse<FileInfoCO> downloadFile(String id,String userId){
        return DataResponse.of(fileService.getFile(id,userId));
    }
}
