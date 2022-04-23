package xyz.leyuna.disk.service.file;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.binary.Base64;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import xyz.leyuna.disk.command.CacheExe;
import xyz.leyuna.disk.command.SliceUploadExe;
import xyz.leyuna.disk.domain.domain.FileInfoE;
import xyz.leyuna.disk.domain.domain.FileUpLogE;
import xyz.leyuna.disk.domain.domain.FileUserE;
import xyz.leyuna.disk.model.DataResponse;
import xyz.leyuna.disk.model.co.FileInfoCO;
import xyz.leyuna.disk.model.co.FileUpLogCO;
import xyz.leyuna.disk.model.co.FileUserCO;
import xyz.leyuna.disk.model.constant.ServerCode;
import xyz.leyuna.disk.model.dto.file.UpFileDTO;
import xyz.leyuna.disk.model.enums.ErrorEnum;
import xyz.leyuna.disk.model.enums.FileEnum;
import xyz.leyuna.disk.model.enums.SortEnum;
import xyz.leyuna.disk.util.AssertUtil;
import xyz.leyuna.disk.util.FileUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.locks.LockSupport;

/**
 * @author LeYuna
 * @email 365627310@qq.com
 * @create 2021-12-24 16:55
 * 文件相关服务 [非查询]
 */
@Service
@Log4j2
public class FileService {

    @Autowired
    private CacheExe cacheExe;

    //默认最大520000
    @Value("${disk.max.memory:520000}")
    private Long maxMemory;

    //默认大小5000
    @Value("${disk.max.file:5000}")
    private Long maxFile;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private SliceUploadExe sliceUploadExe;

    /**
     * 分片上传
     * @param upFileDTO
     * @return
     */
    public DataResponse sliceUploadFile(UpFileDTO upFileDTO){
        return sliceUploadExe.sliceUpload(upFileDTO);
    }

    /**
     * 上传文件 【最普通模式】
     *
     * @param upFileDTO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public DataResponse savaFile(UpFileDTO upFileDTO) {
        //上传用户编号
        String userId = upFileDTO.getUserId();
        try {
            MultipartFile file = upFileDTO.getFile();
            String name = file.getOriginalFilename();
            String type = name.substring(name.lastIndexOf('.') + 1);
            //文件类型
            FileEnum fileEnum = FileEnum.loadType(type);

            //计算K级内存大小
            Long fileSize = file.getSize();
            AssertUtil.isFalse(fileSize > maxMemory, ErrorEnum.FILE_USER_OVER.getName());
            //用户的文件列表
            List<FileUpLogCO> fileUpLogCOS = FileUpLogE.queryInstance().setUserId(userId).selectByConOrder(SortEnum.UPDATE_DESC);
            FileUpLogCO lastFile = null;
            if (CollectionUtil.isNotEmpty(fileUpLogCOS)) {
                lastFile = fileUpLogCOS.get(0);
                //大于5G非法
                if (lastFile.getUpFileTotalSize() + fileSize > maxMemory) {
                    //用户列表内存已满，无法继续上传文件
                    FileUpLogE.queryInstance()
                            .setId(lastFile.getId())
                            .setUpSign(1).update();
                    AssertUtil.isFalse(true, ErrorEnum.FILE_USER_OVER.getName());
                }
            }
            //当前操作后用户的所有文件内存
            Long sizetotal = ObjectUtil.isEmpty(lastFile) ? fileSize : lastFile.getUpFileTotalSize() + fileSize;

            String saveId = FileInfoE.queryInstance()
                    .setFileSize(fileSize)
                    .setSaveDt(StrUtil.isEmpty(upFileDTO.getSaveTime()) ? "永久保存" : upFileDTO.getSaveTime())
                    .setName(file.getOriginalFilename())
                    .setFileType(fileEnum.getValue()).save();
            //如果保存的文件非永久，则进行一个度的校验
            if (StrUtil.isNotEmpty(upFileDTO.getSaveTime())) {

                DateTimeFormatter saveTime = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate ld = LocalDate.parse(upFileDTO.getSaveTime(), saveTime);
                LocalDateTime ldt = LocalDateTime.of(ld, LocalDateTime.now().toLocalTime());
                Duration duration = Duration.between(LocalDateTime.now(), ldt);
                long saveSec = duration.getSeconds();
                //如果文件大小符合条件存入缓存，则进人redis流程
                if (fileSize <= maxFile) {
                    String base64 = null;
                    base64 = Base64.encodeBase64String(file.getBytes());

                    AssertUtil.isFalse(StrUtil.isEmpty(base64), ErrorEnum.FILE_UPLOAD_FILE.getName());
                    //计算存储时间 将小量文件存入redis中进行保存
                    cacheExe.setCacheKey("disk_file:" + saveId, base64, saveSec);
                    //TODO 缓存过期事件，更新数据库
                } else {
                    //TODO 走定时任务，到过期时间时，将存储的文件删除

                    //暂时进行文件存储  在redis中添加一个记号
                    cacheExe.setCacheKey("disk_deleted:" + saveId, "DELETED", saveSec);

                    File saveFile = new File(ServerCode.FILE_ADDRESS + userId + "/" + fileEnum.getName() + "/" + name);
                    if (!saveFile.getParentFile().exists()) {
                        saveFile.getParentFile().mkdirs();
                    }
                    file.transferTo(saveFile);
                }
            } else {
                //如果需要的是永久保存 如果小于5G 则进行累计计算，并将文件转入磁盘中
                File saveFile = new File(ServerCode.FILE_ADDRESS + userId + "/" + fileEnum.getName() + "/" + name);
                if (!saveFile.getParentFile().exists()) {
                    saveFile.getParentFile().mkdirs();
                }
                file.transferTo(saveFile);
            }
            if (null == lastFile) {
                FileUpLogE.queryInstance().setUpFileTotalSize(sizetotal).setUpSign(0).setUserId(userId).save();
            } else {
                FileUpLogE.queryInstance().setId(lastFile.getId()).setUpFileTotalSize(sizetotal).update();
            }
        } catch (IOException e) {
            log.error(e);
            AssertUtil.isFalse(true, ErrorEnum.SERVER_ERROR.getName());
        }
        return DataResponse.buildSuccess();
    }

    /**
     * 删除文件
     *
     * @param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public DataResponse deleteFile(String fileId, String userId) {

        //检查本文件是否属于操作用户
        FileUserCO fileUserCO = FileUserE.queryInstance().setFileId(fileId).setUserId(userId).selectOne();
        AssertUtil.isFalse(ObjectUtil.isEmpty(fileUserCO), ErrorEnum.USER_INFO_ERROR.getName());

        //检查用户上传目录是否存在/正常
        FileUpLogCO fileUpLogCO = FileUpLogE.queryInstance().setUserId(userId).selectOne();
        AssertUtil.isFalse(ObjectUtil.isEmpty(fileUpLogCO), ErrorEnum.SELECT_NOT_FOUND.getName());

        //逻辑删除数据条
        FileUserE.queryInstance().setId(fileUserCO.getId()).setDeleted(1).update();

        //物理删除文件:先检查这个文件是否除了自己 所有用户都不可用了
        List<FileUserCO> fileUserCOS = FileUserE.queryInstance().setFileId(fileId).selectByCon();
        FileInfoCO fileInfoCO = FileInfoE.queryInstance().setId(fileId).selectById();
        if (fileUserCOS.size() == 1) {
            //删除本文件
            FileUtil.deleteFile(fileInfoCO.getFilePath());
        }

        //计算用户的新内存总值
        Long newSize = fileUpLogCO.getUpFileTotalSize() - fileInfoCO.getFileSize();
        FileUpLogE.queryInstance().setId(fileUpLogCO.getId()).setUpFileTotalSize(newSize).update();
        //删除完成
        return DataResponse.buildSuccess();
    }

    /**
     * 获取文件 普通下载
     *
     * @param fileId 文件id
     * @return
     */
    public FileInfoCO getFile(String fileId, String userId) {

        //检查本文件是否属于操作用户
        FileUserCO fileUserCO = FileUserE.queryInstance().setFileId(fileId).setUserId(userId).selectOne();
        AssertUtil.isFalse(ObjectUtil.isEmpty(fileUserCO), ErrorEnum.USER_INFO_ERROR.getName());

        //获取文件数据
        FileInfoCO fileInfoCO = FileInfoE.queryInstance().setId(fileId).selectById();
        AssertUtil.isFalse(ObjectUtil.isEmpty(fileInfoCO), ErrorEnum.SELECT_NOT_FOUND.getName());

        File file = FileUtil.getFile(fileInfoCO.getFilePath());
        AssertUtil.isFalse(ObjectUtil.isEmpty(file), ErrorEnum.SELECT_NOT_FOUND.getName());

        //文件转换成数组byte
        byte[] bytes = FileUtil.FiletoByte(file);
        fileInfoCO.setBase64File(bytes);
        return fileInfoCO;
    }

    /**
     * 获取文件 断点下载
     */
    public void continueDownload(){

    }
}
