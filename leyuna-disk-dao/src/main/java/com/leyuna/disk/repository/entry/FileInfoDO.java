package com.leyuna.disk.repository.entry;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import lombok.Getter;
import lombok.Setter;

/**
 * (FileInfo)表实体类
 *
 * @author pengli
 * @since 2021-12-28 09:45:22
 */
@Getter
@Setter
@TableName("file_info")
public class FileInfoDO implements Serializable {
    private static final long serialVersionUID = 761022345317399509L;
    private String id;

    private String name;

    private LocalDateTime createDt;

    private LocalDateTime updateDt;

    private Integer deleted;

    private Long fileSize;

    private String userId;

    private Long fileSizeTotal;

    /**
     * 文件类型：1图片、2音视、3文档、4其他文件
     */
    private Integer fileType;

}
