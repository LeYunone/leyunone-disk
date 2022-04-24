package xyz.leyuna.disk.service.file;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xyz.leyuna.disk.command.CacheExe;
import xyz.leyuna.disk.domain.domain.FileMd5E;
import xyz.leyuna.disk.domain.domain.FileUserE;
import xyz.leyuna.disk.model.DataResponse;
import xyz.leyuna.disk.model.co.FileMd5CO;
import xyz.leyuna.disk.model.co.FileValidatorCO;
import xyz.leyuna.disk.util.MD5Util;
import xyz.leyuna.disk.validator.FileValidator;
import xyz.leyuna.disk.validator.UserValidator;

import java.util.List;
import java.util.UUID;

/**
 * @author LeYuna
 * @email 365627310@qq.com
 * @date 2022-04-21
 */
@Service
public class ValidatorService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private FileValidator fileValidator;

    @Autowired
    private CacheExe cacheExe;

    /**
     * 校验本次文件上传请求是否合法
     *
     * @param
     * @return
     */
    public DataResponse<FileValidatorCO> judgeFile (String userId,MultipartFile multipartFile){
        FileValidatorCO result = new FileValidatorCO();
        //首先看这个用户是否符合上传文件规则
        userValidator.validator(userId);

        if (ObjectUtil.isEmpty(multipartFile)) {
            return DataResponse.buildFailure();
        }
        //MultipartFile 转化为File
        Integer resultType = 1;
        try {

            String md5 = MD5Util.fileToMD5(multipartFile.getBytes());
            List<FileMd5CO> fileMd5COS = FileMd5E.queryInstance().setMd5Code(md5).selectByCon();

            //说明改文件在服务器中已经有备份
            if(CollectionUtil.isNotEmpty(fileMd5COS)){
                String fileId = fileMd5COS.get(0).getFileId();
                //在本次用户下声明该文件
                FileUserE.queryInstance().setUserId(userId).setFileId(fileId).save();
                //返回给前端：不用继续操作
                resultType = 0;
            }else{
                //继续操作，上传文件，交给前端本次文件标识key
                UUID uuid = UUID.randomUUID();
                result.setFileKey(uuid.toString());
                //在redis中存入本次上传文件的钥匙
                cacheExe.setFileMD5Key(userId,uuid.toString(),md5);
            }

        } catch (Exception e) {
            //如果发生转化异常，本次校验视为通过，上传文件
            logger.error(e.getMessage());
        }finally {
            //封装结果集
            result.setResponseType(resultType);
            return DataResponse.of(result);
        }
    }

}
