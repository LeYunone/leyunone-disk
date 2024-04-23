package com.leyunone.disk.model.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author LeYunone
 * @email 365627310@qq.com
 * @date 2022-04-21
 * 文件校验返回值
 */
@Getter
@Setter
@ToString
public class FileValidatorVO {

    /**
     * 文件唯一标识
     */
    private String identifier;

    /**
     * 返回编码
     */
    private Integer responseType;

}
