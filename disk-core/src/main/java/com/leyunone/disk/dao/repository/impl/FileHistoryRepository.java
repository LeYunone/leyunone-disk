package com.leyunone.disk.dao.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leyunone.disk.common.enums.UploadStageEnum;
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

import java.time.LocalDateTime;
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

    @Override
    public FileHistoryDO selectByUploadId(String uploadId, String diskEnv) {
        LambdaQueryWrapper<FileHistoryDO> lambda = new QueryWrapper<FileHistoryDO>().lambda();
        lambda.eq(FileHistoryDO::getUploadId, uploadId);
        lambda.eq(FileHistoryDO::getEnv, diskEnv);
        return this.baseMapper.selectOne(lambda);
    }

    @Override
    public FileHistoryDO selectStartUpload(String uploadId, String diskEnv) {
        LambdaQueryWrapper<FileHistoryDO> lambda = new QueryWrapper<FileHistoryDO>().lambda();
        lambda.eq(FileHistoryDO::getUploadId, uploadId);
        lambda.eq(FileHistoryDO::getEnv, diskEnv);
        lambda.eq(FileHistoryDO::getStatus, UploadStageEnum.START.getStatus());
        return this.baseMapper.selectOne(lambda);
    }

    @Override
    public List<FileHistoryDO> selectByLtDate(Integer status, LocalDateTime dateTime) {
        LambdaQueryWrapper<FileHistoryDO> lambda = new QueryWrapper<FileHistoryDO>().lambda();
        lambda.eq(FileHistoryDO::getStatus, status);
        lambda.le(FileHistoryDO::getCreateDt, dateTime);
        return this.baseMapper.selectList(lambda);
    }

    @Override
    public List<FileHistoryDO> selectComplete(List<String> uploadIds) {
        LambdaQueryWrapper<FileHistoryDO> lambda = new QueryWrapper<FileHistoryDO>().lambda();
        lambda.in(FileHistoryDO::getUploadId, uploadIds);
        lambda.eq(FileHistoryDO::getStatus, UploadStageEnum.COMPLETE.getStatus());
        return this.baseMapper.selectList(lambda);
    }
}
