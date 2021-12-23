package com.leyuna.disk.repository;

import com.leyuna.disk.co.FileInfoCO;
import com.leyuna.disk.gateway.FileInfoGateway;
import com.leyuna.disk.entry.FileInfoDO;
import com.leyuna.disk.repository.mapper.FileInfoMapper;
import org.springframework.stereotype.Service;

/**
 * (FileInfo)表服务实现类
 *
 * @author pengli
 * @since 2021-12-21 15:29:09
 */
@Service
public class FileInfoRepository extends BaseRepository<FileInfoMapper, FileInfoDO, FileInfoCO>
        implements FileInfoGateway {
}
