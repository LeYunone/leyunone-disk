package xyz.leyuna.disk.dao.repository.entry;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

/**
 * (FileUpLog)表实体类
 *
 * @author LeYuna
 * @email 365627310@qq.com
 * @since 2022-04-21 15:26:51
 */
@Getter
@Setter
@TableName("file_up_log")
public class FileUpLogDO implements Serializable {
    private static final long serialVersionUID = -90243198053359655L;
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    private String userId;

    private Integer upSign;

    private Long upFileTotalSize;

    @TableField(value = "update_Dt", fill = FieldFill.INSERT)
    private LocalDateTime updateDt;

    @TableField(value = "create_Dt", fill = FieldFill.INSERT)
    private LocalDateTime createDt;

}
