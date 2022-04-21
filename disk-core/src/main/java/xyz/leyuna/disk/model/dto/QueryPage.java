package xyz.leyuna.disk.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author LeYuna
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

    //排序标准
    private Object orderCondition;
}
