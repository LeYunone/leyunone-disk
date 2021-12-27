package com.leyuna.disk.co;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * (FileUpLog)出参
 *
 * @author pengli
 * @since 2021-12-27 15:01:44
 */
@Getter
@Setter
public class FileUpLogCO implements Serializable {
    private static final long serialVersionUID = 620969943496973627L;

    private String id;

    private String userId;

    private LocalDateTime updateDt;

    private LocalDateTime createDt;

    /**
     * 最后一次上传的合法标志
     */
    private Integer upSign;
}
