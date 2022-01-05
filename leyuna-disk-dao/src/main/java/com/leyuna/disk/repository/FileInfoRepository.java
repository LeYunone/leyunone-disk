package com.leyuna.disk.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leyuna.disk.co.FileInfoCO;
import com.leyuna.disk.gateway.FileInfoGateway;
import com.leyuna.disk.repository.entry.FileInfoDO;
import com.leyuna.disk.repository.mapper.FileInfoMapper;
import com.leyuna.disk.util.TransformationUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (FileInfo)表服务实现类
 *
 * @author pengli
 * @since 2021-12-27 15:02:59
 */
@Service
public class FileInfoRepository extends BaseRepository<FileInfoMapper, FileInfoDO, FileInfoCO>
        implements FileInfoGateway {
    @Override
    public List<FileInfoCO> selectByUserIdMaxSize (String userId) {
        LambdaQueryWrapper<FileInfoDO> fileInfoDOLambdaQueryWrapper = new QueryWrapper<FileInfoDO>().lambda().eq(FileInfoDO::getUserId, userId).orderByDesc(FileInfoDO::getFileSizeTotal);
        List<FileInfoDO> fileInfoDOS = this.baseMapper.selectList(fileInfoDOLambdaQueryWrapper);
        return TransformationUtil.copyToLists(fileInfoDOS,FileInfoCO.class);
    }
}
