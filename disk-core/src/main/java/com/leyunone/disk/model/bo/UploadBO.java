package com.leyunone.disk.model.bo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

/**
 * :)
 *  上传结果集
 * @Author LeYunone
 * @Date 2024/4/22 17:57
 */
@Getter
@Setter
public class UploadBO {
    
    private boolean success;
    
    private String filePath;
    
    private String fileName;

    private String identifier;

    private Long totalSize;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 目标目录
     */
    private Integer parentId;
}
