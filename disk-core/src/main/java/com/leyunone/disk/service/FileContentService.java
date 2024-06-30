package com.leyunone.disk.service;

import com.leyunone.disk.model.bo.UploadBO;
import com.leyunone.disk.model.vo.FileInfoVO;

/**
 * :)
 *
 * @author LeYunone
 * @email 365627310@qq.com
 * @date 2024/7/1
 */
public interface FileContentService {

    void saveFileContent(UploadBO uploadInfo);

    FileInfoVO fileDetail(String fileId);
}
