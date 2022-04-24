package xyz.leyuna.disk.dao.repository.entry;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * (FileMd5)表实体类
 *
 * @author LeYuna
 * @email 365627310@qq.com
 * @since 2022-04-21 15:26:43
 */
@Getter
@Setter
@TableName("file_md5")
public class FileMd5DO implements Serializable {
    private static final long serialVersionUID = -98943689704162453L;
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    private String fileId;

    /**
     * 文件MD5编码
     */
    private String md5Code;

    /**
     * 存在状态 0 1
     */
    @TableField(value = "deleted", fill = FieldFill.INSERT)
    private Integer deleted;

    @TableField(value = "create_Dt", fill = FieldFill.INSERT)
    private LocalDateTime createDt;

    @TableField(value = "update_Dt", fill = FieldFill.INSERT)
    private LocalDateTime updateDt;

}
