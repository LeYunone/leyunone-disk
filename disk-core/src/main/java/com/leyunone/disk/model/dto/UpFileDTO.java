package com.leyunone.disk.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Transient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * @author LeYunone
 * @email 365627310@qq.com
 * @create 2021-12-24 16:51
 * 上传文件
 */
@Getter
@Setter
@ToString
public class UpFileDTO implements Serializable {

    /**
     * 当前分片下标
     */
    private Integer chunkNumber;

    /**
     * 上传id
     */
    private String uploadId;

    /**
     * 分片大小
     */
    private Integer chunkSize;

    /**
     * 当前分片大小
     */
    private Integer currentChunkSize;

    /**
     * 总文件大小
     */
    private Long totalSize;

    /**
     * 文件唯一标识
     */
    private String identifier;

    /**
     * 文件名
     */
    private String filename;

    /**
     * 总分片数
     */
    private Integer totalChunks;

    @Transient
    private MultipartFile file;

    /**
     * 文件类型
     */
    private String fileType;

    private Integer fileFolderId;
}
