package xyz.leyuna.disk.dao.repository;

import org.springframework.stereotype.Service;
import xyz.leyuna.disk.dao.repository.entry.FileInfoDO;
import xyz.leyuna.disk.dao.repository.mapper.FileInfoMapper;
import xyz.leyuna.disk.domain.domain.FileInfoE;
import xyz.leyuna.disk.domain.gateway.FileInfoGateway;
import xyz.leyuna.disk.model.co.FileInfoCO;
import xyz.leyuna.disk.util.TransformationUtil;

/**
 * (FileInfo)表服务实现类
 *
 * @author pengli
 * @since 2021-12-27 15:02:59
 */
@Service
public class FileInfoRepository extends BaseRepository<FileInfoMapper, FileInfoDO, FileInfoCO>
        implements FileInfoGateway {

    @Override
    public String save (FileInfoE fileInfoE) {
        FileInfoDO fileInfoDO = TransformationUtil.copyToDTO(fileInfoE, FileInfoDO.class);
        this.baseMapper.insert(fileInfoDO);
        return fileInfoDO.getId();
    }
}
