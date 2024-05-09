package com.leyunone.disk.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * :)
 *
 * @Author LeYunone
 * @Date 2024/4/25 14:10
 */
@Getter
@Setter
public class FileFolderVO {

    private String folderName;

    private Integer size;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateDt;

    private String fileName;
    
    private String fileId;
    
    private boolean isFolder;
    
    private Integer fileType;

    private Integer folderId;
    
    private String fileSize;

    private String filePath;
}
