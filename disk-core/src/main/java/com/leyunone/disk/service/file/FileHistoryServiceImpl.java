package com.leyunone.disk.service.file;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.leyunone.disk.common.UploadContext;
import com.leyunone.disk.common.enums.UploadStageEnum;
import com.leyunone.disk.dao.entry.FileHistoryDO;
import com.leyunone.disk.dao.repository.FileHistoryDao;
import com.leyunone.disk.model.bo.UploadBO;
import com.leyunone.disk.service.FileHistoryService;
import com.leyunone.disk.util.BusinessIdLock;
import com.leyunone.disk.util.CollectionFunctionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sun.nio.ch.ThreadPool;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * :)
 *
 * @author LeYunone
 * @email 365627310@qq.com
 * @date 2024/7/29
 */
@Service
public class FileHistoryServiceImpl implements FileHistoryService {

    private final FileHistoryDao fileHistoryDao;
    @Value("${disk.env:oss}")
    private String diskEnv;
    @Value("${disk.storage.day:2}")
    private Integer storageDay;
    @Autowired
    private ThreadPoolExecutor recordPool;

    private static final String UPLOAD_REMARK = "sharding:";

    public FileHistoryServiceImpl(FileHistoryDao fileHistoryDao) {
        this.fileHistoryDao = fileHistoryDao;
    }


    @Override
    public void burialPoint(String uploadId, String fileKey, String md5) {
        FileHistoryDO files = fileHistoryDao.selectStartUpload(uploadId, diskEnv);
        if (ObjectUtil.isNotNull(files)) {
            //极端uploadId重复不考虑
            return;
        }
        FileHistoryDO fileHistoryDO = new FileHistoryDO();
        fileHistoryDO.setUploadId(uploadId);
        fileHistoryDO.setStatus(UploadStageEnum.START.getStatus());
        fileHistoryDO.setEnv(diskEnv);
        fileHistoryDO.setMd5(md5);
        fileHistoryDO.setFileKey(fileKey);
        fileHistoryDao.save(fileHistoryDO);
    }

    @Override
    public void uploadRecord(UploadBO uploadBO) {
        recordPool.submit(() -> {
            BusinessIdLock.lock(uploadBO.getUploadId());
            try {
                FileHistoryDO fileHistory = fileHistoryDao.selectStartUpload(uploadBO.getUploadId(), diskEnv);
                if (ObjectUtil.isNotNull(fileHistory)) {
                    //上传阶段
                    FileHistoryDO currentUpload = new FileHistoryDO();
                    currentUpload.setFileId(fileHistory.getFileId());
                    //记录完成合并的分片是第几片
                    currentUpload.setRemark(UPLOAD_REMARK + uploadBO.getChunkNumber());
                    currentUpload.setUploadId(fileHistory.getUploadId());
                    //完成或小分片
                    currentUpload.setStatus(uploadBO.isSuccess() ? UploadStageEnum.COMPLETE.getStatus() : UploadStageEnum.UPLOADING.getStatus());
                    currentUpload.setEnv(diskEnv);
                    currentUpload.setFileKey(fileHistory.getFileKey());
                    fileHistoryDao.save(currentUpload);
                }
            } finally {
                BusinessIdLock.unLock(uploadBO.getUploadId());
            }
        });
    }

    @Override
    public List<FileHistoryDO> incompleteUpload() {
        //找到day天前开始上传的文件
        LocalDateTime localDateTime = LocalDateTime.now().plusDays(-storageDay);
        List<FileHistoryDO> fileHistoryDOS = fileHistoryDao.selectByLtDate(UploadStageEnum.START.getStatus(), localDateTime);
        if (CollectionUtil.isNotEmpty(fileHistoryDOS)) {
            List<String> uploadIds = fileHistoryDOS.stream().map(FileHistoryDO::getUploadId).collect(Collectors.toList());
            //找到未完成的
            List<FileHistoryDO> completeIds = fileHistoryDao.selectComplete(uploadIds);
            if (CollectionUtil.isNotEmpty(completeIds)) {
                Map<String, FileHistoryDO> completeMap = CollectionFunctionUtils.mapTo(completeIds, FileHistoryDO::getHistoryId);
                fileHistoryDOS = fileHistoryDOS.stream().filter(f -> !completeMap.containsKey(f.getUploadId())).collect(Collectors.toList());
            }
        }
        return fileHistoryDOS;
    }
}
