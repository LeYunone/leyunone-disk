package com.leyuna.disk.repository.entry;

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
 * @since 2021-12-21 14:53:08
 */
@Getter
@Setter
@TableName("file_info")
public class FileInfoDO implements Serializable {
    private static final long serialVersionUID = 262268160287048283L;
    private Integer id;

    private String name;

    private LocalDateTime createDt;

}
