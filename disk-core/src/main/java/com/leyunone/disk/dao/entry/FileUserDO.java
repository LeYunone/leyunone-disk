package com.leyunone.disk.dao.entry;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * (FileUser)表实体类
 *
 * @author LeYunone
 * @email 365627310@qq.com
 * @since 2022-04-21 15:26:57
 */
@Getter
@Setter
@TableName("file_user")
public class FileUserDO implements Serializable {
    private static final long serialVersionUID = -92939058208505262L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String fileId;

    private String userId;

    private String fileFolderId;

    @TableField(value = "deleted", fill = FieldFill.INSERT)
    private Integer deleted;

    @TableField(value = "create_dt", fill = FieldFill.INSERT)
    private LocalDateTime createDt;

    @TableField(value = "update_dt", fill = FieldFill.INSERT)
    private LocalDateTime updateDt;

}
