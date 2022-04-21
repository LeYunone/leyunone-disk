package xyz.leyuna.disk.model.co;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * (FileUpLog)出参
 *
 * @author pengli
 * @since 2022-01-18 15:24:31
 */
@Getter
@Setter
public class FileUpLogCO implements Serializable {
    private static final long serialVersionUID = 216795435215501021L;

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
