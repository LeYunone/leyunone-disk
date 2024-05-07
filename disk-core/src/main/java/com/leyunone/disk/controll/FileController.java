package com.leyunone.disk.controll;

import com.leyunone.disk.model.DataResponse;
import com.leyunone.disk.model.dto.FileDTO;
import com.leyunone.disk.model.dto.FileFolderDTO;
import com.leyunone.disk.model.dto.UpFileDTO;
import com.leyunone.disk.model.query.FileQuery;
import com.leyunone.disk.model.vo.DownloadFileVO;
import com.leyunone.disk.model.vo.UserFileInfoVO;
import com.leyunone.disk.service.FileQueryService;
import com.leyunone.disk.service.FileService;
import com.leyunone.disk.service.UploadPreService;
import com.leyunone.disk.service.front.UploadPreServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
    @Resource
    private FileService fileService;
    @Autowired
    private UploadPreService uploadPreService;

    /**
     * 查询服务器内文件[条件-分页]
     *
     * @return
     */
    @GetMapping("/getFiles")
    public DataResponse<UserFileInfoVO> getFiles(FileQuery query) {
        UserFileInfoVO files = fileQueryService.getFiles(query);
        return DataResponse.of(files);
    }

    /**
     * 存储文件
     */
    @PostMapping("/upload")
    public DataResponse<?> uploadFile(UpFileDTO upFileDTO) {
        fileService.upload(upFileDTO);
        return DataResponse.of();
    }


    /**
     * 校验分片
     *
     * @param upFileDTO
     * @return
     */
    @GetMapping("/upload")
    public DataResponse<?> checkFile(UpFileDTO upFileDTO) {
        uploadPreService.checkFile(upFileDTO);
        return DataResponse.of();
    }

    @PostMapping("/newFolder")
    public DataResponse<?> newFolder(@RequestBody FileFolderDTO fileFolderDTO) {
        fileService.createFolder(fileFolderDTO);
        return DataResponse.of();
    }

    /**
     * 删除指定文件
     *
     * @param
     * @return
     */
    @PostMapping("/delete")
    public DataResponse<?> deleteFile(@RequestBody FileDTO fileDTO) {
        fileService.delete(fileDTO);
        return DataResponse.of();
    }

    /**
     * 下载文件
     *
     * @param
     * @return
     */
    @PostMapping("/download")
    public DataResponse<DownloadFileVO> downloadFile(@RequestBody FileDTO fileDTO) {
        DownloadFileVO down = fileService.down(fileDTO.getFolderId());
        return DataResponse.of(down);
    }
}
