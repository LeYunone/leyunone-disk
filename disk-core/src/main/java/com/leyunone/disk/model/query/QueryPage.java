package com.leyunone.disk.model.query;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author LeYunone
 * @email 365627310@qq.com
 * @create 2021-12-09 16:19
 */
@Getter
@Setter
@ToString
@Accessors( chain = true)
public class QueryPage {

    private Integer index=1;

    private Integer size=10;

    /**
     * 排序字段 1：创建时间 2：更新时间
     */
    private Integer sortField;

    /**
     * 排序方式 1：正序 2：倒序
     */
    private Integer sortMode;
}
