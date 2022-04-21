package xyz.leyuna.disk.dao.repository.entry;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * (FileUpLog)表实体类
 *
 * @author pengli
 * @since 2022-01-18 15:24:24
 */
@Getter
@Setter
@TableName("file_up_log")
public class FileUpLogDO implements Serializable {
    private static final long serialVersionUID = 441097096600639955L;

    @TableId(value = "id",type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 用户
     */
    private String userId;

    /**
     * 更新时间
     */
    @TableField(value = "create_Dt", fill = FieldFill.INSERT)
    private LocalDateTime createDt;

    @TableField(value = "update_Dt", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateDt;

    /**
     * 最后一次上传的合法标志
     */
    private Integer upSign;

    /**
     * 当前操作后的文件总内存
     */
    private Double upFileTotalSize;

}
