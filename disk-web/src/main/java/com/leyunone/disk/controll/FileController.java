package com.leyunone.disk.controll;

import com.leyunone.disk.model.DataResponse;
import com.leyunone.disk.model.co.FileValidatorCO;
import com.leyunone.disk.model.co.UserFileInfoCO;
import com.leyunone.disk.model.dto.DownloadFileDTO;
import com.leyunone.disk.model.dto.FileDTO;
import com.leyunone.disk.model.dto.UpFileDTO;
import com.leyunone.disk.service.file.FileQueryService;
import com.leyunone.disk.service.file.FileService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author LeYunone
 * @email 365627310@qq.com
 * @create 2021-12-09 10:38
 * 文件方接口
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileQueryService fileQueryService;
    @Autowired
    private FileService2 fileService2;

    @Autowired
    private ValidatorService validatorService;

    /**
     * 查询服务器内文件[条件-分页]
     * @return
     */
    @GetMapping("/selectFile")
    public DataResponse<UserFileInfoCO> selectFileList(FileDTO file){
        return fileQueryService.selectFile(file);
    }

    @GetMapping("/selectUserFileSize")
    public DataResponse<Long> selectUserFileSize(String userId){
        return fileQueryService.selectAllFileSizeByUserId(userId);
    }

    /**
     * 请求存储文件 >存储文件的前提
     * @return
     */
    @PostMapping("/requestSaveFile")
    public DataResponse<FileValidatorCO> requestSaveFile(@RequestPart MultipartFile file){
        return validatorService.judgeFile(fileFolderId,userId,file);
    }

    @PostMapping("/deleteTempFile")
    public DataResponse deleteTempFile(String tempPath){
        return fileService2.deleteTempFile(tempPath);
    }

    /**
     * 校验分片
     * @param upFileDTO
     * @return
     */
    @GetMapping("/uploadFile")
    public DataResponse checkFile(UpFileDTO upFileDTO){
        return validatorService.checkFile(upFileDTO);
    }

    /**
     * 存储文件
     */
    @PostMapping("/uploadFile")
    public DataResponse saveFile(UpFileDTO upFileDTO){
        return fileService2.savaFile(upFileDTO);
    }

    @PostMapping("/newFolder")
    public DataResponse newFolder(@RequestBody UpFileDTO upFileDTO){
        return fileService2.savaFile(upFileDTO);
    }

    /**
     * 删除指定文件
     * @param
     * @return
     */
    @PostMapping("/deleteFile")
    public DataResponse deleteFile(@RequestBody FileDTO fileDTO){
        return fileService2.deleteFile(fileDTO);
    }

    /**
     * 下载文件
     * @param
     * @return
     */
    @PostMapping("/downloadFile")
    public void downloadFile(@RequestBody DownloadFileDTO fileDTO){
        fileService2.downloadFile(fileDTO);
    }
}
