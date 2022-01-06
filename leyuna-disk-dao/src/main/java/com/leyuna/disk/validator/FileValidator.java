package com.leyuna.disk.validator;

import com.leyuna.disk.enums.ErrorEnum;
import com.leyuna.disk.util.AssertUtil;
import com.leyuna.disk.util.StringUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author pengli
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
