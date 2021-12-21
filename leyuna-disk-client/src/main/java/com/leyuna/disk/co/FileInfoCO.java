package com.leyuna.disk.co;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * (FileInfo)出参
 *
 * @author pengli
 * @since 2021-12-21 14:50:05
 */
@Getter
@Setter
public class FileInfoCO implements Serializable {
    private static final long serialVersionUID = 551152105629043876L;

    private Integer id;

    private String name;

    private LocalDateTime createDt;
}
