package com.leyuna.disk.repository;

import com.leyuna.disk.co.FileUpLogCO;
import com.leyuna.disk.gateway.FileUpLogGateway;
import com.leyuna.disk.repository.entry.FileUpLogDO;
import com.leyuna.disk.repository.mapper.FileUpLogMapper;
import org.springframework.stereotype.Service;

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
