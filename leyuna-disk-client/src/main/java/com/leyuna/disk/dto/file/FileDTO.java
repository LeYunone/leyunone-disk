package com.leyuna.disk.dto.file;

import com.leyuna.disk.dto.QueryPage;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author pengli
 * @create 2021-12-09 16:20
 *  文件操作类
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class FileDTO extends QueryPage {

    private String id;

    private String name;

    private String userId;
}
