package xyz.leyuna.disk.dao.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import xyz.leyuna.disk.dao.repository.entry.FileInfoDO;
import xyz.leyuna.disk.model.co.FileInfoCO;
import xyz.leyuna.disk.model.dto.file.FileDTO;

import java.util.List;


/**
 * (FileInfo)表数据库访问层
 *
 * @author LeYuna
 * @email 365627310@qq.com
 * @since 2022-04-21 15:49:25
 */
public interface FileInfoMapper extends BaseMapper<FileInfoDO> {

    List<FileInfoCO> selectFileInfoByUser(@Param("con") FileDTO fileDTO);
}

