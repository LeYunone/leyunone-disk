package xyz.leyuna.disk.dao.repository;

import org.springframework.stereotype.Service;
import xyz.leyuna.disk.dao.repository.entry.FileInfoDO;
import xyz.leyuna.disk.dao.repository.mapper.FileInfoMapper;
import xyz.leyuna.disk.domain.gateway.FileInfoGateway;
import xyz.leyuna.disk.model.co.FileInfoCO;

/**
 * (FileInfo)表服务实现类
 *
 * @author pengli@asiainfo.com
 * @since 2022-04-21 15:26:37
 */
@Service
public class FileInfoRepository extends BaseRepository<FileInfoMapper, FileInfoDO, FileInfoCO>
        implements FileInfoGateway {
}
