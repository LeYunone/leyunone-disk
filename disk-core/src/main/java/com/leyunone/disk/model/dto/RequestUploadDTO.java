package com.leyunone.disk.model.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * :)
 *
 * @Author LeYunnoe
 * @Date 2024/5/7 11:18
 */
@Getter
@Setter
public class RequestUploadDTO {

    private Integer folderId;
    
    private String fileName;
    
    private String uniqueIdentifier;

}
