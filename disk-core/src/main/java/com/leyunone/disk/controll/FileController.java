package com.leyunone.disk.controll;

import cn.hutool.core.util.ObjectUtil;
import com.leyunone.disk.common.enums.CheckTypeEnum;
import com.leyunone.disk.model.DataResponse;
import com.leyunone.disk.model.ResponseCode;
import com.leyunone.disk.model.bo.UploadBO;
import com.leyunone.disk.model.dto.FileDTO;
import com.leyunone.disk.model.dto.FileFolderDTO;
import com.leyunone.disk.model.dto.UpFileDTO;
import com.leyunone.disk.model.query.FileQuery;
import com.leyunone.disk.model.vo.CheckFileVO;
import com.leyunone.disk.model.vo.DownloadFileVO;
import com.leyunone.disk.model.vo.FileInfoVO;
import com.leyunone.disk.model.vo.UserFileInfoVO;
import com.leyunone.disk.service.*;
import com.leyunone.disk.service.front.UploadPreServiceImpl;
import com.leyunone.disk.util.AssertUtil;
import com.leyunone.disk.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author LeYunone
 * @email 365627310@qq.com
 * @create 2021-12-09 10:38
 * 文件方接口
 */
@RestController
@RequestMapping("/file")
public class FileController {

    private final Logger logger = LoggerFactory.getLogger(FileController.class);
    @Autowired
    private FileQueryService fileQueryService;
    @Resource
    private FileService fileService;
    @Autowired
    private UploadPreService uploadPreService;
    @Autowired
    private FolderService folderService;
    @Autowired
    private FileContentService fileContentService;


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
     * 查询详情
     * @param fileId
     * @return
     */
    @GetMapping("/detail")
    public DataResponse<FileInfoVO> detail(String fileId) {
        FileInfoVO fileInfoVO = fileContentService.fileDetail(fileId);
        return DataResponse.of(fileInfoVO);
    }

    /**
     * 存储文件
     */
    @PostMapping("/upload")
    public DataResponse<String> uploadFile(UpFileDTO upFileDTO) {
        logger.info("start upload file:{}", upFileDTO.getIdentifier());
        if (ObjectUtil.isNull(upFileDTO.getParentId())) {
            upFileDTO.setParentId(-1);
        }
        upFileDTO.setFileType(FileUtil.getFileType(upFileDTO.getFile()));
        UploadBO upload = fileService.upload(upFileDTO);
        return DataResponse.of(upload.getFilePath());
    }


    /**
     * 校验分片
     *
     * @param upFileDTO
     * @return
     */
    @GetMapping("/upload")
    public DataResponse<CheckFileVO> checkUploadFile(UpFileDTO upFileDTO) {
        CheckFileVO checkFileVO = uploadPreService.checkFile(upFileDTO);
        return DataResponse.of(checkFileVO);
    }

    @PostMapping("/newFolder")
    public DataResponse<?> newFolder(@RequestBody FileFolderDTO fileFolderDTO) {
        folderService.createFolder(fileFolderDTO);
        return DataResponse.of();
    }

    /**
     * 删除指定文件
     *
     * @param
     * @return
     */
    @PostMapping("/delete")
    public DataResponse<?> deleteFile(@RequestBody List<FileDTO> fileDTO) {
        fileDTO = fileDTO.stream().filter(f -> f.getFolderId() != -1).collect(Collectors.toList());
        fileService.delete(fileDTO);
        return DataResponse.of();
    }

    /**
     * 下载文件
     *
     * @param
     * @return
     */
    @PostMapping("/downloadHttp")
    public DataResponse<DownloadFileVO> downloadFileHttp(@RequestBody FileDTO fileDTO) {
        DownloadFileVO down = fileService.down(fileDTO.getFolderId());
        return DataResponse.of(down);
    }

    /**
     * 下载文件
     *
     * @param
     * @return
     */
    @PostMapping("/downloadStream")
    public void downloadFileStream(@RequestBody FileDTO fileDTO) {
        fileService.down(fileDTO.getFolderId());
    }

    /**
     * 取消下载
     *
     * @param upFileDTO
     * @return
     */
    @PostMapping("/cancelUpload")
    public DataResponse<?> cancelUpload(@RequestBody UpFileDTO upFileDTO) {
        fileService.cancelUpload(upFileDTO);
        return DataResponse.of();
    }
}
