package com.leyuna.disk.co;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * (FileInfo)出参
 *
 * @author pengli
 * @since 2021-12-27 15:16:42
 */
@Getter
@Setter
public class FileInfoCO implements Serializable {
    private static final long serialVersionUID = -64841337752222553L;

    private String id;

    private String name;

    private LocalDateTime createDt;

    private LocalDateTime updateDt;

    private Integer deleted;

    private Long fileSize;

    private String userId;

    private Long fileSizeTotal;
}
