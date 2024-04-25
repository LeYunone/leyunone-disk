package com.leyunone.disk.service.file;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.aliyun.oss.model.PartETag;
import com.leyunone.disk.common.UploadContext;
import com.leyunone.disk.common.constant.ServerCode;
import com.leyunone.disk.common.enums.FileTypeEnum;
import com.leyunone.disk.dao.entry.FileInfoDO;
import com.leyunone.disk.dao.repository.FileFolderDao;
import com.leyunone.disk.dao.repository.FileInfoDao;
import com.leyunone.disk.manager.OssManager;
import com.leyunone.disk.model.DataResponse;
import com.leyunone.disk.model.ResponseCode;
import com.leyunone.disk.model.bo.UploadBO;
import com.leyunone.disk.model.co.FileUpLogCO;
import com.leyunone.disk.model.dto.UpFileDTO;
import com.leyunone.disk.util.AssertUtil;
import com.leyunone.disk.util.FileUtil;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.LockSupport;

/**
 * :)
 * 阿里云oss服务
 *
 * @Author pengli
 * @Date 2024/4/22 17:42
 */
public class AliOssFileService extends AbstractFileService {

    @Autowired
    private OssManager ossManager;

    public AliOssFileService(FileInfoDao fileInfoDao, FileFolderDao fileFolderDao) {
        super(fileInfoDao, fileFolderDao);
    }

    @Override
    public UploadBO uploadFile(UpFileDTO upFileDTO) {
        UploadBO uploadBO = new UploadBO();
        uploadBO.setSuccess(false);
        //本次文件的MD5码
        String md5 = upFileDTO.getIdentifier();
        AssertUtil.isFalse(StrUtil.isBlank(md5), "md5 is empty");

        //必须求出redis中的PartETags，在分片合成文件中需要以此为依据，合并文件返回最终地址
        UploadContext.Content content = UploadContext.get(upFileDTO.getUploadId());
        AssertUtil.isFalse(ObjectUtil.isNull(content), ResponseCode.UPLOAD_FAIL);
        MultipartFile file = upFileDTO.getFile();
        int currentChunkNo = upFileDTO.getChunkNumber();
        int totalChunks = upFileDTO.getTotalChunks();
        String ossSlicesId = upFileDTO.getUploadId();
        //字节流转换
        Map<Integer, PartETag> partETags = content.getPartETags();
        //分片上传
        try {
            //每次上传分片之后，OSS的返回结果会包含一个PartETag
            PartETag partETag = ossManager.partUploadFile(upFileDTO.getIdentifier(), file.getInputStream(), ossSlicesId,
                    upFileDTO.getIdentifier(), currentChunkNo, file.getSize());
            partETags.put(currentChunkNo, partETag);
            //分片编号等于总片数的时候合并文件,如果符合条件则合并文件，否则继续等待
            if (currentChunkNo == totalChunks) {
                //合并文件，注意：partETags必须是所有分片的所以必须存入redis，然后取出放入集合
                String url = ossManager.completePartUploadFile(upFileDTO.getIdentifier(), ossSlicesId, new ArrayList<>(partETags.values()));
                //oss地址返回后存入并清除redis
                UploadContext.remove(ossSlicesId);
                uploadBO.setSuccess(true);
                uploadBO.setFilePath(url);
                uploadBO.setFileName(file.getOriginalFilename());
            } else {
                content.setPartETags(partETags);
                UploadContext.set(upFileDTO.getUploadId(), content);
            }
        } catch (Exception e) {
        }
        return uploadBO;
    }

    @Override
    protected String downFile(FileInfoDO fileInfo) {
        return fileInfo.getFilePath();
    }

    @Override
    protected void deleteFile(String fileId) {
        
    }

    /**
     * 请求oss分片上传id
     *
     * @param fileName
     * @return
     */
    @Override
    public String requestUpload(String fileName) {
        String uploadId = ossManager.getUploadId(fileName);
        UploadContext.Content content = new UploadContext.Content();
        content.setPartETags(new HashMap<>());
        return uploadId;
    }
}
