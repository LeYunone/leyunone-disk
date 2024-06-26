package com.leyunone.disk.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leyunone.disk.dao.base.IBaseRepository;
import com.leyunone.disk.dao.entry.FileFolderDO;
import com.leyunone.disk.model.query.FileQuery;
import com.leyunone.disk.model.vo.FileFolderVO;

import java.util.List;


/**
 * (FileInfo)表服务接口
 *
 * @author LeYunone
 * @email 365627310@qq.com
 * @since 2022-04-21 15:26:36
 */
public interface FileFolderDao extends IBaseRepository<FileFolderDO> {

    Page<FileFolderVO> selectPage(FileQuery query);

    FileFolderDO selectByFileIdParentId(String fileId,Integer parentId);

    List<FileFolderDO> selectByFileIds(List<String> fileIds);

    List<FileFolderDO> selectFolder();

    FileFolderDO selectByNameAndParentId(String folderName, Integer parentId);
}
