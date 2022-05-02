package xyz.leyuna.disk.dao.repository;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.stereotype.Service;
import xyz.leyuna.disk.dao.repository.entry.FileUserDO;
import xyz.leyuna.disk.dao.repository.mapper.FileUserMapper;
import xyz.leyuna.disk.domain.domain.FileUserE;
import xyz.leyuna.disk.domain.gateway.FileUserGateway;
import xyz.leyuna.disk.model.co.FileUserCO;

/**
 * (FileUser)表服务实现类
 *
 * @author LeYuna
 * @email 365627310@qq.com
 * @since 2022-04-21 15:26:58
 */
@Service
public class FileUserRepository extends BaseRepository<FileUserMapper, FileUserDO, FileUserCO>
        implements FileUserGateway {
    @Override
    public boolean deleteFolderCFile(String userId, String fileFolderId) {
        UpdateWrapper<FileUserDO> updateWrapper = new UpdateWrapper();
        LambdaUpdateWrapper<FileUserDO> set = updateWrapper.lambda().eq(FileUserDO::getUserId, userId)
                .eq(FileUserDO::getFileFolderId, fileFolderId).set(FileUserDO::getDeleted, 1);
        return this.update(set);
    }
}
