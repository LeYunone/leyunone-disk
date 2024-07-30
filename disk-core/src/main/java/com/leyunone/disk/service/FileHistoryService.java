package com.leyunone.disk.service;

import com.leyunone.disk.dao.entry.FileHistoryDO;
import com.leyunone.disk.model.bo.UploadBO;

import java.util.List;

/**
 * :)
 *
 * @Author pengli
 * @Date 2024/7/30 11:23
 */
public interface FileHistoryService {

    /**
     * 埋点记录
     *
     * @param uploadId
     */
    void burialPoint(String uploadId, String fileKey, String md5);

    /**
     * 上传记录
     *
     * @param uploadBO
     */
    void uploadRecord(UploadBO uploadBO);

    List<FileHistoryDO> incompleteUpload();
}
