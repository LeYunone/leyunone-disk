package xyz.leyuna.disk.controll;

import xyz.leyuna.disk.model.DataResponse;
import xyz.leyuna.disk.model.co.FileInfoCO;
import xyz.leyuna.disk.model.co.FileValidatorCO;
import xyz.leyuna.disk.model.co.UserFileInfoCO;
import xyz.leyuna.disk.model.dto.file.FileDTO;
import xyz.leyuna.disk.model.dto.file.UpFileDTO;
import xyz.leyuna.disk.service.file.FileQueryService;
import xyz.leyuna.disk.service.file.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.leyuna.disk.service.file.ValidatorService;

import java.lang.reflect.Proxy;

/**
 * @author LeYuna
 * @email 365627310@qq.com
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
    public DataResponse<FileValidatorCO> requestSaveFile(@RequestParam(value = "fileFolderId",required = false)String fileFolderId,
                                                         @RequestParam(value = "userId",required = true) String userId,
                                                         @RequestPart MultipartFile file){
        return validatorService.judgeFile(fileFolderId,userId,file);
    }

    @PostMapping("/deleteTempFile")
    public DataResponse deleteTempFile(String tempPath){
        return fileService.deleteTempFile(tempPath);
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
        return fileService.savaFile(upFileDTO);
    }

    @PostMapping("/newFolder")
    public DataResponse newFolder(@RequestBody UpFileDTO upFileDTO){
        return fileService.savaFile(upFileDTO);
    }

    /**
     * 删除指定文件
     * @param id
     * @return
     */
    @PostMapping("/deleteFile")
    public DataResponse deleteFile(@RequestBody FileDTO fileDTO){
        return fileService.deleteFile(fileDTO);
    }

    /**
     * 下载文件
     * @param id
     * @return
     */
    @PostMapping("/downloadFile")
    public DataResponse<FileInfoCO> downloadFile(String id, String userId){
        return DataResponse.of(fileService.getFile(id,userId));
    }
}
