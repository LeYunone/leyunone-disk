package com.leyuna.disk.repository.entry;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.*;
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
    @TableId(value = "id",type = IdType.ID_WORKER_STR)
    private String id;

    private String name;

    @TableField(value = "create_Dt", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime createDt;

    @TableField(value = "update_Dt", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateDt;

    @TableField(value = "deleted" ,fill = FieldFill.INSERT_UPDATE)
    private Integer deleted;

    private Double fileSize;

    private String userId;

    /**
     * 文件类型：1图片、2音视、3文档、4其他文件
     */
    private Integer fileType;

}
