package com.leyuna.disk.repository;

import com.leyuna.disk.co.FileCO;
import com.leyuna.disk.repository.entry.FileDO;
import com.leyuna.disk.repository.mapper.FileMapper;
import com.leyuna.disk.gateway.FileGateway;

/**
 * @author pengli
 * @create 2021-12-13 11:10
 */
public class FileRepository extends BaseRepository<FileMapper, FileDO, FileCO>
        implements FileGateway {

}
