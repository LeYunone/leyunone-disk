package com.leyuna.disk.repository.entry;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

/**
 * (FileUpLog)表实体类
 *
 * @author pengli
 * @since 2021-12-27 15:01:51
 */
@Getter
@Setter
@TableName("file_up_log")
public class FileUpLogDO implements Serializable {
    private static final long serialVersionUID = -33134633636025573L;

    @TableId(value = "id",type = IdType.ID_WORKER_STR)
    private String id;

    private String userId;

    @TableField(value = "update_Dt", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateDt;
    @TableField(value = "create_Dt", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime createDt;

    /**
     * 最后一次上传的合法标志
     */
    private Integer upSign;

}
