package com.leyunone.disk.dao.repository.impl;

import com.leyunone.disk.dao.base.BaseRepository;
import com.leyunone.disk.dao.repository.FileMd5Dao;
import org.springframework.stereotype.Service;
import com.leyunone.disk.dao.entry.FileMd5DO;
import com.leyunone.disk.dao.mapper.FileMd5Mapper;

/**
 * (FileMd5)表服务实现类
 *
 * @author LeYunone
 * @email 365627310@qq.com
 * @since 2022-04-21 15:26:44
 */
@Service
public class FileMd5Repository extends BaseRepository<FileMd5Mapper, FileMd5DO, Object>
        implements FileMd5Dao {
}
