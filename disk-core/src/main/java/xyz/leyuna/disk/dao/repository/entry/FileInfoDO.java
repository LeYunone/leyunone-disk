package xyz.leyuna.disk.dao.repository.entry;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

/**
 * (FileInfo)表实体类
 *
 * @author pengli@asiainfo.com
 * @since 2022-04-29 11:00:13
 */
@Getter
@Setter
@TableName("file_info")
public class FileInfoDO implements Serializable {
    private static final long serialVersionUID = 749605286640812056L;
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 文件名
     */
    private String name;

    /**
     * 文件大小 单位为B
     */
    private Long fileSize;

    /**
     * 文件类型
     */
    private Integer fileType;

    /**
     * 保存时间
     */
    private String saveDt;

    @TableField(value = "update_Dt", fill = FieldFill.INSERT)
    private LocalDateTime updateDt;

    @TableField(value = "create_Dt", fill = FieldFill.INSERT)
    private LocalDateTime createDt;

    /**
     * 0存在 1 删除
     */
    @TableField(value = "deleted", fill = FieldFill.INSERT)
    private Integer deleted;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 上一层父类文件夹ID
     */
    private String fileFolderId;

}
