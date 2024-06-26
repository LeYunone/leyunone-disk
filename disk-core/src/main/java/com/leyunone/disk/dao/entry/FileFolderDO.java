package com.leyunone.disk.dao.entry;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * :)
 *
 * @Author LeYunone
 * @Date 2024/4/22 17:29
 */
@Getter
@Setter
@TableName("file_folder")
public class FileFolderDO {

    @TableId(value = "folder_id", type = IdType.AUTO)
    private Integer folderId;

    private String folderName;

    private boolean isFolder;

    private Integer parentId;

    private String fileId;

    @TableField(value = "update_Dt", fill = FieldFill.INSERT)
    private LocalDateTime updateDt;

    @TableField(exist = false)
    private String fileName;
    @TableField(exist = false)
    private Integer fileSize;
    @TableField(exist = false)
    private Integer fileType;
}
