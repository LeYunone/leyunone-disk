package com.leyunone.disk.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leyunone.disk.dao.base.IBaseRepository;
import com.leyunone.disk.dao.entry.FileInfoDO;
import com.leyunone.disk.model.query.FileQuery;


/**
 * (FileInfo)表服务接口
 *
 * @author LeYunone
 * @email 365627310@qq.com
 * @since 2022-04-21 15:26:36
 */
public interface FileInfoDao extends IBaseRepository<FileInfoDO> {

    Page<FileInfoDO> selectByConPage(FileQuery query);
    
    FileInfoDO selectByMd5(String md5);
}
