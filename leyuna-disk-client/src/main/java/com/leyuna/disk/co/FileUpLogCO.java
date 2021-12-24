package com.leyuna.disk.co;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * (FileUpLog)出参
 *
 * @author pengli
 * @since 2021-12-24 17:13:32
 */
@Getter
@Setter
public class FileUpLogCO implements Serializable {
    private static final long serialVersionUID = 346000585648870950L;

    private String id;

    private String ip;

    private LocalDateTime updateDt;

    private String createDt;
}
