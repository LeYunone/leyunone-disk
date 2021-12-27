package com.leyuna.disk.repository.entry;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.FieldFill;
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
    private String id;

    private String userId;

    private LocalDateTime updateDt;

    private LocalDateTime createDt;

    /**
     * 最后一次上传的合法标志
     */
    private Integer upSign;

}
