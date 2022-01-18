package com.leyuna.disk.gateway;

import com.leyuna.disk.co.FileInfoCO;
import com.leyuna.disk.domain.FileInfoE;

import java.util.List;

/**
 * (FileInfo)表服务接口
 *
 * @author pengli
 * @since 2021-12-21 14:54:49
 */
public interface FileInfoGateway extends BaseGateway<FileInfoCO> {

    String save(FileInfoE fileInfoE);
}
