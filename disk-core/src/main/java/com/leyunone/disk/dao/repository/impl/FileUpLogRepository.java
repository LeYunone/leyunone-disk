package com.leyunone.disk.dao.repository.impl;

import com.leyunone.disk.dao.base.BaseRepository;
import com.leyunone.disk.dao.repository.FileUpLogDao;
import org.springframework.stereotype.Service;
import com.leyunone.disk.dao.entry.FileUpLogDO;
import com.leyunone.disk.dao.mapper.FileUpLogMapper;

/**
 * (FileUpLog)表服务实现类
 *
 * @author LeYunone
 * @email 365627310@qq.com
 * @since 2022-04-21 15:26:52
 */
@Service
public class FileUpLogRepository extends BaseRepository<FileUpLogMapper, FileUpLogDO, Object>
        implements FileUpLogDao {
}
