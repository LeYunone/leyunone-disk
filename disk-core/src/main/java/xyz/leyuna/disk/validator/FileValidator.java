package xyz.leyuna.disk.validator;

import org.springframework.web.multipart.MultipartFile;
import xyz.leyuna.disk.model.enums.ErrorEnum;
import xyz.leyuna.disk.util.AssertUtil;
import xyz.leyuna.disk.util.StringUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * @author LeYuna
 * @email 365627310@qq.com
 * @create 2021-12-27 10:54
 * 检查文件合法性
 */
@Component
public class FileValidator {

    public void validator(String fileName, Double size, MultipartFile file){
        if(ObjectUtils.isEmpty(file)){
            //检查文件名和文件大小
            AssertUtil.isFalse(StringUtil.isBanks(fileName), ErrorEnum.FILE_NAME_EMPTY.getName());
        }else{
            //检查文件类
        }
    }
}
