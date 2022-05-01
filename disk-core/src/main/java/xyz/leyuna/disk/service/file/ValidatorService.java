package xyz.leyuna.disk.service.file;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
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
import xyz.leyuna.disk.model.dto.file.UpFileDTO;
import xyz.leyuna.disk.model.enums.ErrorEnum;
import xyz.leyuna.disk.util.AssertUtil;
import xyz.leyuna.disk.util.FileUtil;
import xyz.leyuna.disk.util.MD5Util;
import xyz.leyuna.disk.validator.FileValidator;
import xyz.leyuna.disk.validator.UserValidator;

import java.io.File;
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
     * 校验本次文件上传请求是否合法或进行秒传操作
     *
     * @param
     * @return
     */
    public DataResponse<FileValidatorCO> judgeFile (String fileFolderId,String userId,MultipartFile multipartFile){
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
                FileUserE.queryInstance().setUserId(userId).setFileId(fileId).setFileFolderId(fileFolderId).save();
                //返回给前端：不用继续操作
                resultType = 0;
            }else{
                //继续操作，上传文件，交给前端本次文件标识key
                result.setIdentifier(md5);
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

    /**
     * 校验本次分片是否需要上传 如果服务端中有这个文件则跳过
     * @param upFileDTO
     * @return
     */
    public DataResponse checkFile(UpFileDTO upFileDTO){

        String fileMD5Value = upFileDTO.getIdentifier();

        AssertUtil.isFalse(StrUtil.isBlank(fileMD5Value), ErrorEnum.FILE_UPLOAD_FILE.getName());
        //获得分片文件存储的临时目录
        String tempPath = FileUtil.resoleFileTempPath(fileMD5Value);
        AssertUtil.isFalse(StrUtil.isBlank(tempPath), ErrorEnum.FILE_UPLOAD_FILE.getName());

        //开始进行切片化上传
        File sliceFile = new File(tempPath + upFileDTO.getChunkNumber());
        AssertUtil.isFalse(sliceFile.exists(),ErrorEnum.FILE_UPLOAD_FILE.getName());

        return DataResponse.buildSuccess();
    }
}
