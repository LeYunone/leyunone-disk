package xyz.leyuna.disk.dao.repository.entry;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * (FileUser)表实体类
 *
 * @author LeYuna
 * @email 365627310@qq.com
 * @since 2022-04-21 15:26:57
 */
@Getter
@Setter
@TableName("file_user")
public class    FileUserDO implements Serializable {
    private static final long serialVersionUID = -92939058208505262L;
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    private String fileId;

    private String userId;

    @TableField(value = "deleted", fill = FieldFill.INSERT)
    private Integer deleted;

    @TableField(value = "create_Dt", fill = FieldFill.INSERT)
    private LocalDateTime createDt;

    @TableField(value = "update_Dt", fill = FieldFill.INSERT)
    private LocalDateTime updateDt;

}
