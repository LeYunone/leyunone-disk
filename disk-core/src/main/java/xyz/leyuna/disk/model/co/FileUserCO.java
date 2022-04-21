package xyz.leyuna.disk.model.co;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * (FileUser)出参
 *
 * @author LeYuna
 * @email 365627310@qq.com
 * @since 2022-04-21 15:26:58
 */
@Getter
@Setter
public class FileUserCO implements Serializable {
    private static final long serialVersionUID = -37130021088269140L;

    private String id;

    private String fileId;

    private String userId;

    private Integer deleted;

    private LocalDateTime createDt;

    private LocalDateTime updateDt;
}
