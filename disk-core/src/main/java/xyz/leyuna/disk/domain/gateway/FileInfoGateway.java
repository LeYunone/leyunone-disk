package xyz.leyuna.disk.domain.gateway;


import xyz.leyuna.disk.domain.domain.FileInfoE;
import xyz.leyuna.disk.model.co.FileInfoCO;

/**
 * (FileInfo)表服务接口
 *
 * @author pengli
 * @since 2021-12-21 14:54:49
 */
public interface FileInfoGateway extends BaseGateway<FileInfoCO> {

    String save(FileInfoE fileInfoE);
}
