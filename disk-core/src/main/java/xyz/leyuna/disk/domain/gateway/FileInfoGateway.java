package xyz.leyuna.disk.domain.gateway;

import xyz.leyuna.disk.domain.domain.FileInfoE;
import xyz.leyuna.disk.model.co.FileInfoCO;
import xyz.leyuna.disk.model.dto.file.FileDTO;

import java.util.List;


/**
 * (FileInfo)表服务接口
 *
 * @author LeYuna
 * @email 365627310@qq.com
 * @since 2022-04-21 15:26:36
 */
public interface FileInfoGateway extends BaseGateway<FileInfoCO> {

    String save(FileInfoE fileInfoE);

    List<FileInfoCO> selectFileInfoByUser(FileDTO fileDTO);
}
