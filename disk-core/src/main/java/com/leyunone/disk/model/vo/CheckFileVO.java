package com.leyunone.disk.model.vo;

import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * :)
 *
 * @Author LeYunnoe
 * @Date 2024/5/13 11:37
 */
@Data
public class CheckFileVO {

    /**
     * 跳过上传
     */
    private Boolean skipUpload;
    
    /*
        已上传分片
     */
    private Set<Integer> uploadedChunks;
}
