package com.leyuna.disk.entry;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import lombok.Getter;
import lombok.Setter;

/**
 * (FileInfo)表实体类
 *
 * @author pengli
 * @since 2021-12-21 16:08:02
 */
@Getter
@Setter
@TableName("file_info")
public class FileInfoDO implements Serializable {
    private static final long serialVersionUID = 299448244129723838L;
    private String id;

    private String name;

    private LocalDateTime createDt;

    private LocalDateTime updateDt;

    private Integer deleted;

}
