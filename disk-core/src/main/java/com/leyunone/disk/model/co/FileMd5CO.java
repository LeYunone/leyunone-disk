package com.leyunone.disk.model.co;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * (FileMd5)出参
 *
 * @author LeYunone
 * @email 365627310@qq.com
 * @since 2022-04-21 15:26:44
 */
@Getter
@Setter
public class FileMd5CO implements Serializable {
    private static final long serialVersionUID = 889941739187513198L;

    private String id;

    private String fileId;

    /**
     * 文件MD5编码
     */
    private String md5Code;

    /**
     * 存在状态 0 1
     */
    private Integer deleted;

    private LocalDateTime createDt;

    private LocalDateTime updateDt;
}
