package xyz.leyuna.disk.model.co;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

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
     * 文件大小 单位为B
     */
    private Long fileSize;

    private String fileSizeText;

    /**
     * 文件类型
     */
    private Integer fileType;

    /**
     * 文件类型 中文翻译
     */
    private String fileTypeText;

    /**
     * 保存时间
     */
    private String saveDt;

    private LocalDateTime updateDt;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createDt;

    private Integer deleted;

    private String fileFolderId;

    /**
     * 文件路径
     */
    private String filePath;

    private byte[] base64File;
}
