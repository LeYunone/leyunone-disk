package com.leyuna.disk.co;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * (FileInfo)出参
 *
 * @author pengli
 * @since 2021-12-28 09:45:27
 */
@Getter
@Setter
public class FileInfoCO implements Serializable {
    private static final long serialVersionUID = -92920680899937156L;

    private String id;

    private String name;

    @JsonFormat( pattern = "yyyy-MM-dd")
    private LocalDateTime createDt;

    private LocalDateTime updateDt;

    private Integer deleted;

    private Double fileSize;

    private String userId;

    private String fileTypeName;

    /**
     * 文件类型：1图片、2音乐、3视频、4文档、5其他文件
     */
    private Integer fileType;
}
