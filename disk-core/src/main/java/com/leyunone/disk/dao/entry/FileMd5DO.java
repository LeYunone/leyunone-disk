package com.leyunone.disk.dao.entry;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * (FileMd5)表实体类
 *
 * @author LeYunone
 * @email 365627310@qq.com
 * @since 2022-04-21 15:26:43
 */
@Getter
@Setter
@TableName("file_md5")
public class FileMd5DO implements Serializable {
    private static final long serialVersionUID = -98943689704162453L;
    @TableId(value = "md5")
    private String md5;

    private String fileId;

    /**
     * 存在状态 0 1
     */
    @TableField(value = "deleted", fill = FieldFill.INSERT)
    private Integer deleted;

    @TableField(value = "create_dt", fill = FieldFill.INSERT)
    private LocalDateTime createDt;

    @TableField(value = "update_dt", fill = FieldFill.INSERT)
    private LocalDateTime updateDt;

}
