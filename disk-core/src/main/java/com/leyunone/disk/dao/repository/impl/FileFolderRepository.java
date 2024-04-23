package com.leyunone.disk.dao.repository.impl;

import com.leyunone.disk.dao.base.BaseRepository;
import com.leyunone.disk.dao.entry.FileFolderDO;
import com.leyunone.disk.dao.mapper.FileFolderMapper;
import com.leyunone.disk.dao.repository.FileFolderDao;
import org.springframework.stereotype.Service;

/**
 * (FileFolderDO)表服务实现类
 *
 * @author LeYunone
 * @email 365627310@qq.com
 * @since 2022-04-21 15:26:37
 */
@Service
public class FileFolderRepository extends BaseRepository<FileFolderMapper, FileFolderDO, Object>
        implements FileFolderDao {
}
