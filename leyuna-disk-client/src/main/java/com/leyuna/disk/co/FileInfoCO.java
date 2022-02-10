package com.leyuna.disk.co;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * (FileInfo)出参
 *
 * @author pengli
 * @since 2022-02-10 10:19:51
 */
@Getter
@Setter
public class FileInfoCO implements Serializable {
    private static final long serialVersionUID = -33557261354917777L;

    private String id;

    private String name;

    private LocalDateTime createDt;

    private LocalDateTime updateDt;

    private Integer deleted;

    private Double fileSize;

    private String userId;

    /**
     * 文件类型：1图片、2音视、3文档、4其他文件
     */
    private Integer fileType;

    /**
     * 文件类型中文
     */
    private String fileTypeName;
}
