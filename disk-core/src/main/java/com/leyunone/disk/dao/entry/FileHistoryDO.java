package com.leyunone.disk.dao.entry;

import com.baomidou.mybatisplus.annotation.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * :)
 * 文件上传历史
 *
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

    @TableId(type = IdType.ASSIGN_UUID)
    private String historyId;

    private String fileId;

    private String fileKey;

    private String uploadId;

    /**
     * 状态 0请求上传 1上传阶段 2上传结束
     */
    private Integer status;

    private String remark;

    /**
     * 环境 local/oss
     */
    private String env;

    private String md5;

    @TableField(value = "create_dt", fill = FieldFill.INSERT)
    private LocalDateTime createDt;
}
