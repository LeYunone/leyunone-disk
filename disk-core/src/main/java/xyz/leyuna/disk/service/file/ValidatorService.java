package xyz.leyuna.disk.service.file;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.security.provider.MD5;
import xyz.leyuna.disk.domain.domain.FileInfoE;
import xyz.leyuna.disk.domain.domain.FileMd5E;
import xyz.leyuna.disk.domain.domain.FileUserE;
import xyz.leyuna.disk.model.DataResponse;
import xyz.leyuna.disk.model.co.FileInfoCO;
import xyz.leyuna.disk.model.co.FileMd5CO;
import xyz.leyuna.disk.model.constant.ServerCode;
import xyz.leyuna.disk.model.dto.file.UpFileDTO;
import xyz.leyuna.disk.util.FileUtil;
import xyz.leyuna.disk.util.MD5Util;
import xyz.leyuna.disk.validator.FileValidator;
import xyz.leyuna.disk.validator.UserValidator;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author pengli
 * @date 2022-04-21
 */
@Service
public class ValidatorService {

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private FileValidator fileValidator;

    /**
     * 校验本次文件上传请求是否合法
     *
     * @param upFileDTO
     * @return
     */
    public DataResponse<Integer> judgeFile (UpFileDTO upFileDTO){
        String userId = upFileDTO.getUserId();
        //首先看这个用户是否符合上传文件规则
        userValidator.validator(userId);
        
        MultipartFile multipartFile = upFileDTO.getFile();
        if (ObjectUtil.isEmpty(multipartFile)) {
            return DataResponse.buildFailure();
        }
        //MultipartFile 转化为File
        File file = new File("temp");
        Integer resultType = 1;
        try {
            FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), file);
            
            String md5 = MD5Util.fileToMD5(file);
            List<FileMd5CO> fileMd5COS = FileMd5E.queryInstance().setMd5Code(md5).selectByCon();

            //说明改文件在服务器中已经有备份
            if(CollectionUtil.isNotEmpty(fileMd5COS)){
                String fileId = fileMd5COS.get(0).getFileId();
                //在本次用户下声明该文件
                FileUserE.queryInstance().setUserId(userId).setFileId(fileId).save();
                //返回给前端：不用继续操作
                resultType = 0;
            }
            //继续操作，上传文件
        } catch (IOException e) {
            //如果发生转化异常，本次校验视为通过，上传文件
        }finally {
            if(file.exists()){
                file.delete();
            }
            return DataResponse.of(resultType);
        }
    }

}
