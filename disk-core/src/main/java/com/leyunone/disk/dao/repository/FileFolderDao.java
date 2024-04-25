package com.leyunone.disk.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leyunone.disk.dao.base.IBaseRepository;
import com.leyunone.disk.dao.entry.FileFolderDO;
import com.leyunone.disk.model.query.FileQuery;
import com.leyunone.disk.model.vo.FileFolderVO;


/**
 * (FileInfo)表服务接口
 *
 * @author LeYunone
 * @email 365627310@qq.com
 * @since 2022-04-21 15:26:36
 */
public interface FileFolderDao extends IBaseRepository<FileFolderDO> {

    void deleteByFileId(String fileId);
    
    Page<FileFolderVO> selectPage(FileQuery query);
}
