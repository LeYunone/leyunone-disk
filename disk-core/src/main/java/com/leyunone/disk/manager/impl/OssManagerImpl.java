package com.leyunone.disk.manager.impl;

import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.*;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.leyunone.disk.manager.OssManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.List;

/**
 * @author zhangtonghao
 * @create 2022-08-30 16:26
 */
@Component
public class OssManagerImpl implements OssManager {

    private static final Logger logger = LoggerFactory.getLogger(OssManagerImpl.class);

    @Value("${oss.endpoint:}")
    private String endpoint;
    @Value("${oss.accessKeyId:}")
    private String accessKeyId;
    @Value("${oss.accessKeySecret:}")
    private String accessKeySecret;
    @Value("${oss.bucketName:}")
    private String bucketName;
    @Value("${oss.bucketUrl:}")
    private String bucketUrl;

    private OSS ossClient;

    @PostConstruct
    public void init() {
        if (StringUtils.isBlank(endpoint) || StringUtils.isBlank(accessKeyId) || StringUtils.isBlank(accessKeySecret)) {
            return;
        }
        ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }

    /**
     * 分块上传完成获取结果
     */
    @Override
    public String completePartUploadFile(String fileName, String uploadId, List<PartETag> partETags) {
        CompleteMultipartUploadRequest request = new CompleteMultipartUploadRequest(bucketName, fileName, uploadId,
                partETags);
        ossClient.completeMultipartUpload(request);
        return getDownloadUrl(fileName, bucketName);
    }


    /**
     * @param fileKey  文件名称
     * @param is       文件流数据
     * @param uploadId oss唯一分片id
     * @param fileMd5  文件的md5值（非必传）
     * @param partNum  第几片
     * @param partSize 分片大小
     * @return
     */
    @Override
    public PartETag partUploadFile(String fileKey, InputStream is, String uploadId, String fileMd5, int partNum, long partSize) {
        UploadPartRequest uploadPartRequest = new UploadPartRequest();
        uploadPartRequest.setBucketName(bucketName);
        uploadPartRequest.setUploadId(uploadId);
        uploadPartRequest.setPartNumber(partNum);
        uploadPartRequest.setPartSize(partSize);
        uploadPartRequest.setInputStream(is);
        uploadPartRequest.setKey(fileKey);
        uploadPartRequest.setMd5Digest(fileMd5);
        UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);
        return uploadPartResult.getPartETag();
    }

    /**
     * 分块上传完成获取结果
     */
    @Override
    public String getUploadId(String fileName) {
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, fileName);
        // 初始化分片
        InitiateMultipartUploadResult unrest = ossClient.initiateMultipartUpload(request);
        // 返回uploadId，它是分片上传事件的唯一标识，您可以根据这个ID来发起相关的操作，如取消分片上传、查询分片上传等。
        return unrest.getUploadId();
    }

    /**
     * 获取bucket文件的下载链接
     *
     * @param fileName   首字母不带/的路径和文件
     * @param bucketName
     * @return 上报返回null, 成功返回地址
     */
    private String getDownloadUrl(String fileName, String bucketName) {
        StringBuilder url = new StringBuilder();
        url.append("http://").append(bucketName).append(endpoint).append("/");
        if (fileName != null && !"".equals(fileName)) {
            url.append(fileName);
        }
        return url.toString();
    }
}
