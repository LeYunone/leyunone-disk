package com.leyuna.disk.gateway;

import com.leyuna.disk.co.FileInfoCO;

import java.util.List;

/**
 * (FileInfo)表服务接口
 *
 * @author pengli
 * @since 2021-12-21 14:54:49
 */
public interface FileInfoGateway extends BaseGateway<FileInfoCO> {

    List<FileInfoCO> selectByUserIdMaxSize(String userId);
}
