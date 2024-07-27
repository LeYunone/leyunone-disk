package com.leyunone.disk.dao.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leyunone.disk.dao.base.BaseRepository;
import com.leyunone.disk.dao.entry.FileFolderDO;
import com.leyunone.disk.dao.entry.FileHistoryDO;
import com.leyunone.disk.dao.mapper.FileFolderMapper;
import com.leyunone.disk.dao.mapper.FileHistoryMapper;
import com.leyunone.disk.dao.repository.FileFolderDao;
import com.leyunone.disk.dao.repository.FileHistoryDao;
import com.leyunone.disk.model.query.FileQuery;
import com.leyunone.disk.model.vo.FileFolderVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (FileHistoryDO)表服务实现类
 *
 * @author LeYunone
 * @email 365627310@qq.com
 * @since 2022-04-21 15:26:37
 */
@Service
public class FileHistoryRepository extends BaseRepository<FileHistoryMapper, FileHistoryDO, Object>
        implements FileHistoryDao {

}
