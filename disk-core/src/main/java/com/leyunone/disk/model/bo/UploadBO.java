package com.leyunone.disk.model.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * :)
 *  上传结果集
 * @Author pengli
 * @Date 2024/4/22 17:57
 */
@Getter
@Setter
public class UploadBO {
    
    private boolean success;
    
    private String filePath;
    
    private String fileName;
}
