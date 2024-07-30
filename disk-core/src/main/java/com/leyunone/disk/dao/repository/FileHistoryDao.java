package com.leyunone.disk.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leyunone.disk.dao.base.IBaseRepository;
import com.leyunone.disk.dao.entry.FileFolderDO;
import com.leyunone.disk.dao.entry.FileHistoryDO;
import com.leyunone.disk.model.query.FileQuery;
import com.leyunone.disk.model.vo.FileFolderVO;

import java.time.LocalDateTime;
import java.util.List;


/**
 * (FileInfo)表服务接口
 *
 * @author LeYunone
 * @email 365627310@qq.com
 * @since 2022-04-21 15:26:36
 */
public interface FileHistoryDao extends IBaseRepository<FileHistoryDO> {

    FileHistoryDO selectByUploadId(String uploadId, String diskEnv);

    /**
     * 查询开始的
     * @param uploadId
     * @param diskEnv
     * @return
     */
    FileHistoryDO selectStartUpload(String uploadId, String diskEnv);

    /**
     * 查询小于这个时间的
     * @param status
     * @param dateTime
     * @return
     */
    List<FileHistoryDO> selectByLtDate(Integer status, LocalDateTime dateTime);

    /**
     * 查询已完成的
     * @param uploadIds
     * @return
     */
    List<FileHistoryDO> selectComplete(List<String> uploadIds);
}
