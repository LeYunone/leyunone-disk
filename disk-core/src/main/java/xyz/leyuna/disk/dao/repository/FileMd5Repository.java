package xyz.leyuna.disk.dao.repository;

import org.springframework.stereotype.Service;
import xyz.leyuna.disk.dao.repository.entry.FileMd5DO;
import xyz.leyuna.disk.dao.repository.mapper.FileMd5Mapper;
import xyz.leyuna.disk.domain.gateway.FileMd5Gateway;
import xyz.leyuna.disk.model.co.FileMd5CO;

/**
 * (FileMd5)表服务实现类
 *
 * @author pengli@asiainfo.com
 * @since 2022-04-21 15:26:44
 */
@Service
public class FileMd5Repository extends BaseRepository<FileMd5Mapper, FileMd5DO, FileMd5CO>
        implements FileMd5Gateway {
}
