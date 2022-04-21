package xyz.leyuna.disk.model.co;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * (FileInfo)出参
 *
 * @author LeYuna
 * @email 365627310@qq.com
 * @since 2022-04-21 15:53:42
 */
@Getter
@Setter
public class FileInfoCO implements Serializable {
    private static final long serialVersionUID = -53435262164526348L;

    private String id;

    /**
     * 文件名
     */
    private String name;

    /**
     * 文件大小
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

    private LocalDateTime updateDt;

    private LocalDateTime createDt;

    private Integer deleted;

    /**
     * 文件路径
     */
    private String filePath;

    private byte[] base64File;
}
