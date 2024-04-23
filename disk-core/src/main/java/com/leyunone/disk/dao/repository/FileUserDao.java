package com.leyunone.disk.dao.repository;

import com.leyunone.disk.dao.base.IBaseRepository;
import com.leyunone.disk.dao.entry.FileUserDO;
import com.leyunone.disk.model.co.FileUserCO;


/**
 * (FileUser)表服务接口
 *
 * @author LeYunone
 * @email 365627310@qq.com
 * @since 2022-04-21 15:26:57
 */
public interface FileUserDao extends IBaseRepository<FileUserDO> {

    boolean deleteFolderCFile(String userId,String fileFolderId);
}
