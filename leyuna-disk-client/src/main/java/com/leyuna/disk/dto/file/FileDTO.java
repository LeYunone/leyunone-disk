package com.leyuna.disk.dto.file;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author pengli
 * @create 2021-12-09 16:20
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class FileDTO {

    private String id;
}
