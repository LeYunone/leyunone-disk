package com.leyunone.disk.model.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author LeYunone
 * @email 365627310@qq.com
 * @create 2021-12-09 16:20
 *  文件操作类
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class FileDTO  {

    private String fileId;

    private String fileName;

    private Integer type;

    private Integer fileType;

    private String fileFolderId;
}
