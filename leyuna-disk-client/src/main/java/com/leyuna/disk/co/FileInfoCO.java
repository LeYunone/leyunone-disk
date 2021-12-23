package com.leyuna.disk.co;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * (FileInfo)出参
 *
 * @author pengli
 * @since 2021-12-21 16:08:13
 */
@Getter
@Setter
public class FileInfoCO implements Serializable {
    private static final long serialVersionUID = -57826925195323495L;

    private String id;

    private String name;

    private LocalDateTime createDt;

    private LocalDateTime updateDt;

    private Integer deleted;
}
