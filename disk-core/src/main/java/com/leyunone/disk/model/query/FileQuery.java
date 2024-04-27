package com.leyunone.disk.model.query;

import lombok.Getter;
import lombok.Setter;

/**
 * :)
 *
 * @Author LeYunone
 * @Date 2024/4/22 16:38
 */
@Getter
@Setter
public class FileQuery extends QueryPage {
    
    private String nameCondition;
    
    private Integer fileType;
    
    private Integer fileFolderId;
}
