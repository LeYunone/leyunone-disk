package com.leyunone.disk.dao.entry;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * :)
 *  文件上传历史
 * @author LeYunone
 * @email 365627310@qq.com
 * @date 2024/7/29
 */
@Getter
@Setter
@TableName("file_folder")
@ToString
@EqualsAndHashCode
public class FileHistoryDO {

    private String fileId;

    private String uploadId;

    private Integer status;

    private String remark;

    private LocalDateTime updateTime;
}
