package com.leyunone.disk.model.vo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * :)
 *
 * @Author pengli
 * @Date 2024/4/25 14:10
 */
@Getter
@Setter
public class FileFolderVO {

    private String folderName;

    private Integer size;

    private LocalDateTime updateTime;

    private String fileName;
    
    private String fileId;
    
    private boolean isFolder;
    
    private Integer fileType;

    private Integer folderId;
    
    private String fileSize;
}
