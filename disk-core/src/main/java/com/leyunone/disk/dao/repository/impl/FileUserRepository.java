package com.leyunone.disk.dao.repository.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.leyunone.disk.dao.base.BaseRepository;
import com.leyunone.disk.dao.repository.FileUserDao;
import org.springframework.stereotype.Service;
import com.leyunone.disk.dao.entry.FileUserDO;
import com.leyunone.disk.dao.mapper.FileUserMapper;

/**
 * (FileUser)表服务实现类
 *
 * @author LeYunone
 * @email 365627310@qq.com
 * @since 2022-04-21 15:26:58
 */
@Service
public class FileUserRepository extends BaseRepository<FileUserMapper, FileUserDO, Object>
        implements FileUserDao {
    @Override
    public boolean deleteFolderCFile(String userId, String fileFolderId) {
        UpdateWrapper<FileUserDO> updateWrapper = new UpdateWrapper();
        LambdaUpdateWrapper<FileUserDO> set = updateWrapper.lambda().eq(FileUserDO::getUserId, userId)
                .eq(FileUserDO::getFileFolderId, fileFolderId).set(FileUserDO::getDeleted, 1);
        return this.update(set);
    }
}
