package com.leyunone.disk.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leyunone.disk.dao.entry.FileFolderDO;
import com.leyunone.disk.model.query.FileQuery;
import com.leyunone.disk.model.vo.FileFolderVO;
import org.apache.ibatis.annotations.Param;


/**
 * (FileFolderDO)表数据库访问层
 *
 * @author LeYunone
 * @email 365627310@qq.com
 * @since 2022-04-21 15:49:25
 */
public interface FileFolderMapper extends BaseMapper<FileFolderDO> {

    Page<FileFolderVO> selectFolderPage(@Param("con") FileQuery query,Page page);
}

