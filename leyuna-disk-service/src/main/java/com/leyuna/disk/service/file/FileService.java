package com.leyuna.disk.service.file;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.leyuna.disk.DataResponse;
import com.leyuna.disk.co.FileInfoCO;
import com.leyuna.disk.command.CacheExe;
import com.leyuna.disk.constant.ServerCode;
import com.leyuna.disk.domain.FileInfoE;
import com.leyuna.disk.domain.FileUpLogE;
import com.leyuna.disk.dto.file.UpFileDTO;
import com.leyuna.disk.enums.ErrorEnum;
import com.leyuna.disk.enums.FileEnum;
import com.leyuna.disk.enums.SortEnum;
import com.leyuna.disk.util.AssertUtil;
import com.leyuna.disk.util.FileUtil;
import com.leyuna.disk.util.ObjectUtil;
import com.leyuna.disk.validator.FileValidator;
import com.leyuna.disk.validator.UserValidator;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author pengli
 * @create 2021-12-24 16:55
 * 文件相关服务 [非查询]
 */
@Service
public class FileService {

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private FileValidator fileValidator;

    @Autowired
    private CacheExe cacheExe;


    /**
     * 校验本次文件上传请求是否合法
     *
     * @param upFileDTO
     * @return
     */
    public DataResponse JudgeFile (UpFileDTO upFileDTO) {

        //首先看这个用户是否符合上传文件规则
        userValidator.validator(upFileDTO.getUserId());

        //判断这个文件大小 文件格式  文件名 是否合法
        String fileName = upFileDTO.getFileName();
        Integer fileSize = upFileDTO.getFileSize();
        fileValidator.validator(fileName, fileSize, null);

        //按文件名和大小 判断是否有数据一样的文件
        File file = FileUtil.searchFile(fileName, fileSize);
        if (ObjectUtil.isNotNull(file)) {
            //如果服务器中有一个一模一样的文件，则直接进行拷贝操作
            FileUtil.copyFile(file, upFileDTO.getUserId());
        }

        return DataResponse.buildSuccess();
    }

    /**
     * 保存文件
     *
     * @param upFileDTO
     * @return
     */
    public DataResponse savaFile (UpFileDTO upFileDTO) {
        //上传用户编号
        String userId = upFileDTO.getUserId();
        try {
            MultipartFile[] files = upFileDTO.getFiles();

            for (MultipartFile file : files) {
                String name = file.getName();
                String type = name.substring(name.lastIndexOf('.'), name.length());
                //文件类型
                FileEnum fileEnum = FileEnum.loadType(type);

                //用户的文件列表
                List<FileInfoCO> fileInfoCOS = FileInfoE.queryInstance().setUserId(userId)
                        .selectByConOrder(SortEnum.UPDATE_DESC);
                FileInfoCO lastFile = null;
                if (CollectionUtils.isNotEmpty(fileInfoCOS)) {
                    lastFile=fileInfoCOS.get(0);
                    //大于5G非法
                    if (lastFile.getFileSizeTotal()+file.getSize() > 5000000) {
                        //用户列表内存已满，无法继续上传文件
                        FileUpLogE.queryInstance().setUpdateDt(LocalDateTime.now())
                                .setUpSign(1).setUserId(userId).setCreateDt(LocalDateTime.now()).save();
                        AssertUtil.isFalse(true,ErrorEnum.FILE_USER_OVER.getName());
                    }
                }
                long sizetotal=ObjectUtils.isEmpty(lastFile)?file.getSize():lastFile.getFileSizeTotal()+file.getSize();

                //如果保存的文件非永久，则进行一个度的校验
                if (null != upFileDTO.getSaveTime()) {
                    if (file.getSize() <= 51000) {
                        String base64 = null;
                        base64 = Base64.encode(file.getBytes());

                        if (StringUtils.isNotEmpty(base64)) {
                            //计算存储时间
                            LocalDateTime saveTime = upFileDTO.getSaveTime();
                            Duration duration = Duration.between(saveTime, LocalDateTime.now());
                            long saveSec = duration.getSeconds();
                            //将小量文件存入redis中进行保存
                            cacheExe.setCacheKey("file/" + userId, base64, saveSec);
                        }
                        //TODO 缓存过期事件，更新数据库
                    } else {
                        //TODO 走定时任务，到过期时间时，将存储的文件删除
                    }
                } else {
                    //如果需要的是永久保存 如果小于5G 则进行累计计算，并将文件转入磁盘中
                    file.transferTo(new File(ServerCode.FILE_ADDRESS + userId));
                }

                FileInfoE.queryInstance().setUserId(userId).
                        setCreateDt(LocalDateTime.now()).setDeleted(0).setFileSize(file.getSize())
                        .setFileSizeTotal(sizetotal)
                        .setName(file.getName())
                        .setFileType(fileEnum.getValue())
                        .setUpdateDt(LocalDateTime.now()).save();
            }
        } catch (Exception e) {
            AssertUtil.isFalse(true, ErrorEnum.SERVER_ERROR.getName());
        }
        return DataResponse.buildSuccess();
    }

    /**
     * 删除文件
     *
     * @param id
     * @return
     */
    public DataResponse deleteFile (String id) {

        FileInfoCO fileInfoCO = FileInfoE.queryInstance().setId(id).selectById();
        AssertUtil.isFalse(null == fileInfoCO, ErrorEnum.SELECT_NOT_FOUND.getName());

        //逻辑删除数据条 按照创建时间排序查询 第一条的内存总量就是当前用户的内存总量
        FileInfoE.queryInstance().setId(id).setDeleted(1).update();

        //物理删除文件
        File file = new File(ServerCode.FILE_ADDRESS + fileInfoCO.getUserId() + "/" + fileInfoCO.getName());
        AssertUtil.isFalse(!file.exists(), ErrorEnum.SELECT_NOT_FOUND.getName());
        file.delete();
        return DataResponse.buildSuccess();
    }

    /**
     * 获取文件
     *
     * @param id 文件id
     * @return
     */
    public File getFile (String id) {

        //获取文件数据
        FileInfoCO fileInfoCO = FileInfoE.queryInstance().setId(id).selectById();
        AssertUtil.isFalse(ObjectUtils.isEmpty(fileInfoCO), ErrorEnum.SELECT_NOT_FOUND.getName());

        File file = FileUtil.getFile(fileInfoCO.getName(), fileInfoCO.getUserId(),
                FileEnum.loadName(fileInfoCO.getFileType()).getName());
        return file;

    }
}
