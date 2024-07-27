package com.leyunone.disk.service.file;

import com.leyunone.disk.common.UploadContext;
import com.leyunone.disk.dao.entry.FileHistoryDO;
import com.leyunone.disk.dao.repository.FileHistoryDao;
import org.springframework.stereotype.Service;

/**
 * :)
 *
 * @author LeYunone
 * @email 365627310@qq.com
 * @date 2024/7/29
 */
@Service
public class FileHistoryService {

    private final FileHistoryDao fileHistoryDao;

    public FileHistoryService(FileHistoryDao fileHistoryDao) {
        this.fileHistoryDao = fileHistoryDao;
    }

}
