package com.leyunone.disk.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * :)
 *
 * @Author pengli
 * @Date 2024/4/22 16:49
 */
@Getter
@Setter
public class FileInfoVO {

    private static final long serialVersionUID = -53435262164526348L;

    private String fileId;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件大小 单位为B
     */
    private String fileSize;

    /**
     * 文件类型
     */
    private String fileType;

    private LocalDateTime updateDt;

    private LocalDateTime createDt;

    private Integer deleted;

    private String fileFolderId;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 文件base64流
     */
    private byte[] base64File;
}
