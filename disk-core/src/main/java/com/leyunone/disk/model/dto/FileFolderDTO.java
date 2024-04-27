package com.leyunone.disk.model.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * :)
 *
 * @Author LeYunone
 * @Date 2024/4/22 17:39
 */
@Getter
@Setter
public class FileFolderDTO {

    /**
     * 父文件夹id
     */
    private Integer parentId;

    /**
     * 新文件夹名
     */
    private String newFolderName;
}
