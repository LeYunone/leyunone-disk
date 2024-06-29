package com.leyunone.disk.dao.entry;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * :)
 *
 * @Author LeYunone
 * @Date 2024/4/22 17:29
 */
@Getter
@Setter
@TableName("file_extend_content")
public class FileExtendContentDO {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String fileId;

    private String fileContent;
}
