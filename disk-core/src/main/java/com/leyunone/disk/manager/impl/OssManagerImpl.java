package com.leyunone.disk.manager.impl;

import cn.hutool.core.util.ObjectUtil;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.*;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.leyunone.disk.manager.OssManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * @author zhangtonghao
 * @create 2022-08-30 16:26
 */
@Service
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


    /**
     * 分块上传完成获取结果
     */
    @Override
    public String completePartUploadFile(String fileName, String uploadId, List<PartETag> partETags) {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        CompleteMultipartUploadRequest request = new CompleteMultipartUploadRequest(bucketName, fileName, uploadId,
                partETags);
        ossClient.completeMultipartUpload(request);
        ossClient.shutdown();
        return getDownloadUrl(fileName);
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
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        UploadPartRequest uploadPartRequest = new UploadPartRequest();
        uploadPartRequest.setBucketName(bucketName);
        uploadPartRequest.setUploadId(uploadId);
        uploadPartRequest.setPartNumber(partNum);
        uploadPartRequest.setPartSize(partSize);
        uploadPartRequest.setInputStream(is);
        uploadPartRequest.setKey(fileKey);
//        uploadPartRequest.setMd5Digest(fileMd5);
        UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);
        ossClient.shutdown();
        return uploadPartResult.getPartETag();
    }

    /**
     * 分块上传完成获取结果
     */
    @Override
    public String getUploadId(String fileName) {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, fileName);
        // 初始化分片
        InitiateMultipartUploadResult unrest = ossClient.initiateMultipartUpload(request);
        ossClient.shutdown();
        // 返回uploadId，它是分片上传事件的唯一标识，您可以根据这个ID来发起相关的操作，如取消分片上传、查询分片上传等。
        return unrest.getUploadId();
    }


    @Override
    public String getFileUrl(String name, Long expireTime) {
        if (ObjectUtil.isNull(expireTime)) {
            return this.getDownloadUrl(name);
        }
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 设置URL过期时间为1小时。
        Date expiration = new Date(System.currentTimeMillis() + expireTime);
        // 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
        URL url = ossClient.generatePresignedUrl(bucketName, name, expiration);
        // 关闭OSSClient。
        ossClient.shutdown();
        return url.toString();
    }

    @Override
    public void deleteFile(String fileName) {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 删除文件。如需删除文件夹，请将ObjectName设置为对应的文件夹名称。如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹。
        ossClient.deleteObject(bucketName, fileName);
        // 关闭OSSClient。
        ossClient.shutdown();
    }

    @Override
    public String uploadFile(String name, InputStream stream) {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 创建OSSClient实例。
        // 上传文件到指定的存储空间（bucketName）并将其保存为指定的文件名称（objectName）。
        ossClient.putObject(bucketName, name, stream);
        // 关闭OSSClient。
        ossClient.shutdown();
        return getDownloadUrl(name);
    }

    /**
     * 获取bucket文件的下载链接
     */
    private String getDownloadUrl(String fileName) {
        StringBuilder url = new StringBuilder();
        url.append("https://").append(bucketUrl).append("/");
        if (fileName != null && !"".equals(fileName)) {
            url.append(fileName);
        }
        return url.toString();
    }
}
