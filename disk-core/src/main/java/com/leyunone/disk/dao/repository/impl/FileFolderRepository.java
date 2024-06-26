package com.leyunone.disk.dao.repository.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leyunone.disk.dao.base.BaseRepository;
import com.leyunone.disk.dao.entry.FileFolderDO;
import com.leyunone.disk.dao.mapper.FileFolderMapper;
import com.leyunone.disk.dao.repository.FileFolderDao;
import com.leyunone.disk.model.query.FileQuery;
import com.leyunone.disk.model.vo.FileFolderVO;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public Page<FileFolderVO> selectPage(FileQuery query) {
        Page page = new Page<>(query.getIndex(), query.getSize());
        return this.baseMapper.selectFolderPage(query, page);
    }

    @Override
    public FileFolderDO selectByFileIdParentId(String fileId, Integer parentId) {
        LambdaQueryWrapper<FileFolderDO> lambda = new QueryWrapper<FileFolderDO>().lambda();
        lambda.eq(FileFolderDO::getFileId, fileId);
        lambda.eq(FileFolderDO::getParentId, parentId);
        return this.baseMapper.selectOne(lambda);
    }

    @Override
    public List<FileFolderDO> selectByFileIds(List<String> fileIds) {
        LambdaQueryWrapper<FileFolderDO> lambda = new QueryWrapper<FileFolderDO>().lambda();
        lambda.in(FileFolderDO::getFileId, fileIds);
        return this.baseMapper.selectList(lambda);
    }

    @Override
    public List<FileFolderDO> selectFolder() {
        LambdaQueryWrapper<FileFolderDO> lambda = new QueryWrapper<FileFolderDO>()
                .eq("is_folder", 1)
                .lambda();
        return this.baseMapper.selectList(lambda);
    }

    @Override
    public FileFolderDO selectByNameAndParentId(String folderName, Integer parentId) {
        LambdaQueryWrapper<FileFolderDO> lambda = new QueryWrapper<FileFolderDO>().lambda();
        lambda.eq(FileFolderDO::getFolderName, folderName);
        lambda.eq(FileFolderDO::getParentId, parentId);
        return this.baseMapper.selectOne(lambda);
    }
}
