package xyz.leyuna.disk.service.file;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import xyz.leyuna.disk.command.CacheExe;
import xyz.leyuna.disk.domain.domain.FileInfoE;
import xyz.leyuna.disk.domain.domain.FileUpLogE;
import xyz.leyuna.disk.model.DataResponse;
import xyz.leyuna.disk.model.co.FileInfoCO;
import xyz.leyuna.disk.model.co.FileUpLogCO;
import xyz.leyuna.disk.model.constant.ServerCode;
import xyz.leyuna.disk.model.dto.file.UpFileDTO;
import xyz.leyuna.disk.model.enums.ErrorEnum;
import xyz.leyuna.disk.model.enums.FileEnum;
import xyz.leyuna.disk.model.enums.SortEnum;
import xyz.leyuna.disk.util.AssertUtil;
import xyz.leyuna.disk.util.FileUtil;
import xyz.leyuna.disk.validator.FileValidator;
import xyz.leyuna.disk.validator.UserValidator;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author pengli
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


    /**
     * 保存文件
     *
     * @param upFileDTO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public DataResponse savaFile (UpFileDTO upFileDTO) {
        //上传用户编号
        String userId = upFileDTO.getUserId();
        try {
            MultipartFile file = upFileDTO.getFile();
            String name = file.getOriginalFilename();
            String type = name.substring(name.lastIndexOf('.')+1);
            //文件类型
            FileEnum fileEnum = FileEnum.loadType(type);

            //计算K级内存大小
            double fileSize=(double)file.getSize()/1024;
            AssertUtil.isFalse(fileSize>maxMemory, ErrorEnum.FILE_USER_OVER.getName());
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
            Double sizetotal = ObjectUtil.isEmpty(lastFile) ? fileSize : lastFile.getUpFileTotalSize() + fileSize;

            String saveId = FileInfoE.queryInstance().setUserId(userId)
                    .setFileSize(fileSize)
                    .setSaveDt(StrUtil.isEmpty(upFileDTO.getSaveTime())?"永久保存":upFileDTO.getSaveTime())
                    .setFileTypeName(fileEnum.getName())
                    .setName(file.getOriginalFilename())
                    .setFileTypeName(fileEnum.getName())
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

                    AssertUtil.isFalse(StrUtil.isEmpty(base64),ErrorEnum.FILE_UPLOAD_FILE.getName());
                    //计算存储时间 将小量文件存入redis中进行保存
                    cacheExe.setCacheKey("disk_file:"+saveId, base64, saveSec);
                    //TODO 缓存过期事件，更新数据库
                } else {
                    //TODO 走定时任务，到过期时间时，将存储的文件删除

                    //暂时进行文件存储  在redis中添加一个记号
                    cacheExe.setCacheKey("disk_deleted:"+saveId,"DELETED",saveSec);

                    File saveFile = new File(ServerCode.FILE_ADDRESS + userId+"/"+fileEnum.getName()+"/"+name);
                    if(!saveFile.getParentFile().exists()){
                        saveFile.getParentFile().mkdirs();
                    }
                    file.transferTo(saveFile);
                }
            } else {
                //如果需要的是永久保存 如果小于5G 则进行累计计算，并将文件转入磁盘中
                File saveFile = new File(ServerCode.FILE_ADDRESS + userId+"/"+fileEnum.getName()+"/"+name);
                if(!saveFile.getParentFile().exists()){
                    saveFile.getParentFile().mkdirs();
                }
                file.transferTo(saveFile);
            }
            if(null==lastFile){
                FileUpLogE.queryInstance().setUpFileTotalSize(sizetotal).setUpSign(0).setUserId(userId).save();
            }else{
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
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public DataResponse deleteFile (String id,String userId) {

        FileInfoCO fileInfoCO = FileInfoE.queryInstance().setId(id).selectById();
        AssertUtil.isFalse(!fileInfoCO.getUserId().equals(userId),ErrorEnum.USER_INFO_ERROR.getName());
        AssertUtil.isFalse(null == fileInfoCO, ErrorEnum.SELECT_NOT_FOUND.getName());

        //逻辑删除数据条
        FileInfoE.queryInstance().setId(id).setDeleted(1).update();
        FileEnum fileEnum = FileEnum.loadName(fileInfoCO.getFileType());
        //物理删除文件
        File file = new File(ServerCode.FILE_ADDRESS + fileInfoCO.getUserId() + "/" +fileEnum.getName()+"/"+ fileInfoCO.getName());
        if(file.exists()){
            file.delete();
        }else{
            cacheExe.clearCacheKey("disk_file:"+fileInfoCO.getId());
        }
//        File file = new File(ServerCode.FILE_ADDRESS + fileInfoCO.getUserId() + "/" + fileInfoCO.getName());
//        AssertUtil.isFalse(!file.exists(), ErrorEnum.SELECT_NOT_FOUND.getName());
//        file.delete();

        //计算用户的新内存总值
        List<FileUpLogCO> fileUpLogCOS = FileUpLogE.queryInstance().setUserId(userId).selectByCon();
        AssertUtil.isFalse(CollectionUtil.isEmpty(fileUpLogCOS), ErrorEnum.SELECT_NOT_FOUND.getName());
        FileUpLogCO fileUpLogCO = fileUpLogCOS.get(0);

        //更新最新用户内存上传信息
        Double size=fileUpLogCO.getUpFileTotalSize()-fileInfoCO.getFileSize();
        FileUpLogE.queryInstance().setId(fileUpLogCO.getId()).setUpFileTotalSize(size).update();

        return DataResponse.buildSuccess();
    }

    /**
     * 获取文件
     *
     * @param id 文件id
     * @return
     */
    public FileInfoCO getFile (String id,String userId) {

        //获取文件数据
        FileInfoCO fileInfoCO = FileInfoE.queryInstance().setId(id).selectById();
        AssertUtil.isFalse(ObjectUtil.isEmpty(fileInfoCO), ErrorEnum.SELECT_NOT_FOUND.getName());
        AssertUtil.isFalse(!fileInfoCO.getUserId().equals(userId),ErrorEnum.USER_INFO_ERROR.getName());

        File file = FileUtil.getFile(fileInfoCO.getName(), fileInfoCO.getUserId(),
                FileEnum.loadName(fileInfoCO.getFileType()).getName());
        if(!file.exists()){
            //如果找不到文件，则说明这个文件暂时保存在缓存中
            String base64= cacheExe.getCacheByKey("disk_file:" + fileInfoCO.getId());
            fileInfoCO.setBase64File(Base64.decodeBase64(base64));
        }else{
            //文件转换成数组byte 
            byte[] bytes = FileUtil.FiletoByte(file);
            fileInfoCO.setBase64File(bytes);
        }
        return fileInfoCO;
    }
}
