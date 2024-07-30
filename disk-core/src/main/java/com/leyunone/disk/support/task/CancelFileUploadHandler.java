package com.leyunone.disk.support.task;

import cn.hutool.core.collection.CollectionUtil;
import com.leyunone.disk.common.UploadContext;
import com.leyunone.disk.dao.entry.FileHistoryDO;
import com.leyunone.disk.manager.OssManager;
import com.leyunone.disk.service.FileHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * :)
 * 取消文件上传
 *
 * @Author pengli
 * @Date 2024/7/23 14:42
 */
@Component
public class CancelFileUploadHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final OssManager ossManager;
    private final FileHistoryService fileHistoryService;

    public CancelFileUploadHandler(OssManager ossManager, FileHistoryService fileHistoryService) {
        this.ossManager = ossManager;
        this.fileHistoryService = fileHistoryService;
    }

    @Scheduled(cron = "3 0 1 * * ?")
//    @Scheduled(cron = "1 0/1 * * * ?")
    public void execute() {
        List<FileHistoryDO> fileHistoryDOS = fileHistoryService.incompleteUpload();
        if (CollectionUtil.isNotEmpty(fileHistoryDOS)) {
            fileHistoryDOS.forEach((fileHistory) -> {
                try {
                    ossManager.cancelFile(fileHistory.getFileKey(), fileHistory.getUploadId());
                } catch (Exception e) {
                    logger.error("cancel file error,e:{}", e.getMessage());
                } finally {
                    UploadContext.cleanCache(fileHistory.getUploadId(), fileHistory.getMd5());
                }
            });
        }

    }
}
