package com.leyuna.disk.dao.repository;

import com.leyuna.disk.client.co.FileCO;
import com.leyuna.disk.dao.repository.entry.FileDO;
import com.leyuna.disk.dao.repository.mapper.FileMapper;
import com.leyuna.disk.domain.gateway.FileGateway;

/**
 * @author pengli
 * @create 2021-12-13 11:10
 */
public class FileRepository extends BaseRepository<FileMapper, FileDO, FileCO>
        implements FileGateway {

}
