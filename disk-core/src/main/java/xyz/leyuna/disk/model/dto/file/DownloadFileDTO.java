package xyz.leyuna.disk.model.dto.file;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author LeYuna
 * @email 365627310@qq.com
 * @date 2022-04-23
 *
 * 文件下载
 */
@Getter
@Setter
@ToString
public class DownloadFileDTO implements Serializable {

    private String fileId;

    private String userId;
}
