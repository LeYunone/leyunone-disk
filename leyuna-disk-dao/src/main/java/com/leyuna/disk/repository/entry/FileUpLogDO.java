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
 * @since 2022-01-18 15:24:24
 */
@Getter
@Setter
@TableName("file_up_log")
public class FileUpLogDO implements Serializable {
    private static final long serialVersionUID = 441097096600639955L;
    private String id;

    /**
     * 用户
     */
    private String userId;

    /**
     * 更新时间
     */
    private LocalDateTime updateDt;

    /**
     * 创建时间
     */
    private LocalDateTime createDt;

    /**
     * 最后一次上传的合法标志
     */
    private Integer upSign;

    /**
     * 当前操作后的文件总内存
     */
    private Double upFileTotalSize;

}
