package com.leyunone.disk.model.co;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * (FileUpLog)出参
 *
 * @author LeYunone
 * @since 2022-04-21 15:26:52
 */
@Getter
@Setter
public class FileUpLogCO implements Serializable {
    private static final long serialVersionUID = 960263911959678758L;

    private String id;

    private String userId;

    private Integer upSign;

    private Long upFileTotalSize;

    private LocalDateTime updateDt;

    private LocalDateTime createDt;
}
