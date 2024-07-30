package com.leyunone.disk.dao.entry;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

/**
 * (FileInfo)表实体类
 *
 * @author LeYunone@asiainfo.com
 * @since 2022-04-29 11:00:13
 */
@Getter
@Setter
@TableName("file_info")
public class FileInfoDO implements Serializable {
    private static final long serialVersionUID = 749605286640812056L;
    @TableId(value = "file_id")
    private String fileId;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件大小 单位为B
     */
    private long fileSize;

    /**
     * 文件类型
     */
    private Integer fileType;
    
    private String fileMd5;

    @TableField(value = "update_dt", fill = FieldFill.INSERT)
    private LocalDateTime updateDt;

    @TableField(value = "create_dt", fill = FieldFill.INSERT)
    private LocalDateTime createDt;

    /**
     * 文件路径
     */
    private String filePath;

}
