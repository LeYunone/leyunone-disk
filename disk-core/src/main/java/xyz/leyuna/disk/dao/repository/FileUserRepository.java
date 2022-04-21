package xyz.leyuna.disk.dao.repository;

import org.springframework.stereotype.Service;
import xyz.leyuna.disk.dao.repository.entry.FileUserDO;
import xyz.leyuna.disk.dao.repository.mapper.FileUserMapper;
import xyz.leyuna.disk.domain.gateway.FileUserGateway;
import xyz.leyuna.disk.model.co.FileUserCO;

/**
 * (FileUser)表服务实现类
 *
 * @author LeYuna
 * @email 365627310@qq.com
 * @since 2022-04-21 15:26:58
 */
@Service
public class FileUserRepository extends BaseRepository<FileUserMapper, FileUserDO, FileUserCO>
        implements FileUserGateway {
}
