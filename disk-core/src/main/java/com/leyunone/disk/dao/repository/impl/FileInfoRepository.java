package com.leyunone.disk.dao.repository.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leyunone.disk.dao.base.BaseRepository;
import com.leyunone.disk.dao.repository.FileInfoDao;
import com.leyunone.disk.model.query.FileQuery;
import org.springframework.stereotype.Service;
import com.leyunone.disk.dao.entry.FileInfoDO;
import com.leyunone.disk.dao.mapper.FileInfoMapper;

/**
 * (FileInfo)表服务实现类
 *
 * @author LeYunone
 * @email 365627310@qq.com
 * @since 2022-04-21 15:26:37
 */
@Service
public class FileInfoRepository extends BaseRepository<FileInfoMapper, FileInfoDO, Object>
        implements FileInfoDao {

    @Override
    public Page<FileInfoDO> selectByConPage(FileQuery query) {
        LambdaQueryWrapper<FileInfoDO> lambda = new QueryWrapper<FileInfoDO>().lambda();
        lambda.eq(ObjectUtil.isNotNull(query.getFileType()), FileInfoDO::getFileType, query.getFileType());
        Page<FileInfoDO> page = new Page<>(query.getIndex(), query.getSize());
        return this.baseMapper.selectPage(page, lambda);
    }

    @Override
    public FileInfoDO selectByMd5(String md5) {
        LambdaQueryWrapper<FileInfoDO> lambda = new QueryWrapper<FileInfoDO>().lambda();
        lambda.eq(FileInfoDO::getFileMd5,md5);
        return this.baseMapper.selectOne(lambda);
    }
}
