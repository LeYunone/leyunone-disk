package com.leyunone.disk.validator;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.leyunone.disk.util.AssertUtil;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * @author LeYunone
 * @email 365627310@qq.com
 * @create 2021-12-27 10:54
 * 检查文件合法性
 */
@Component
public class FileValidator {

    public void validator(String fileName, Double size, MultipartFile file) {
        if (ObjectUtils.isEmpty(file)) {
            //检查文件名和文件大小
            AssertUtil.isFalse(StringUtils.isBlank(fileName), "fileName is empty");
        } else {
            //检查文件类
        }
    }
}
