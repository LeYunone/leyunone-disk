package com.leyunone.disk.service.folder;

import cn.hutool.core.util.ObjectUtil;
import com.leyunone.disk.dao.entry.FileFolderDO;
import com.leyunone.disk.dao.repository.FileFolderDao;
import com.leyunone.disk.model.ResponseCode;
import com.leyunone.disk.model.dto.FileFolderDTO;
import com.leyunone.disk.service.FolderService;
import com.leyunone.disk.util.AssertUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * :)
 *
 * @Author LeYunnoe
 * @Date 2024/5/13 15:37
 */
@Service
@RequiredArgsConstructor
public class FolderServiceImpl implements FolderService {

    private final FileFolderDao fileFolderDao;

    @Override
    public void createFolder(FileFolderDTO fileFolderDTO) {
        /**
         * 首页-1
         */
        if (ObjectUtil.isNull(fileFolderDTO.getParentId())) {
            fileFolderDTO.setParentId(-1);
        }
        FileFolderDO fileFolderDO = new FileFolderDO();
        fileFolderDO.setFolderName(fileFolderDTO.getNewFolderName() + "/");
        FileFolderDO exist = fileFolderDao.selectByNameAndParentId(fileFolderDO.getFolderName(), fileFolderDTO.getParentId());
        AssertUtil.isFalse(ObjectUtil.isNotNull(exist), ResponseCode.FOLDER_EXIST);
        fileFolderDO.setFolder(true);
        fileFolderDO.setParentId(fileFolderDTO.getParentId());
        fileFolderDao.save(fileFolderDO);
    }
}
