package xyz.leyuna.disk.dao.repository;

import org.springframework.stereotype.Service;
import xyz.leyuna.disk.dao.repository.entry.FileUpLogDO;
import xyz.leyuna.disk.dao.repository.mapper.FileUpLogMapper;
import xyz.leyuna.disk.domain.gateway.FileUpLogGateway;
import xyz.leyuna.disk.model.co.FileUpLogCO;

/**
 * (FileUpLog)表服务实现类
 *
 * @author pengli
 * @since 2021-12-27 15:01:53
 */
@Service
public class FileUpLogRepository extends BaseRepository<FileUpLogMapper, FileUpLogDO, FileUpLogCO>
        implements FileUpLogGateway {
}
