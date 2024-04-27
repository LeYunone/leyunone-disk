package com.leyunone.disk.service.front;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.aliyun.oss.model.PartETag;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.leyunone.disk.common.UploadContext;
import com.leyunone.disk.dao.entry.FileInfoDO;
import com.leyunone.disk.dao.entry.FileMd5DO;
import com.leyunone.disk.dao.repository.FileInfoDao;
import com.leyunone.disk.dao.repository.FileMd5Dao;
import com.leyunone.disk.model.ResponseCode;
import com.leyunone.disk.model.dto.UpFileDTO;
import com.leyunone.disk.model.vo.FileValidatorVO;
import com.leyunone.disk.service.FileService;
import com.leyunone.disk.service.UploadPreService;
import com.leyunone.disk.util.AssertUtil;
import com.leyunone.disk.util.MD5Util;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * :)
 *
 * @Author LeYunone
 * @Date 2024/4/22 17:12
 */
@Service
@RequiredArgsConstructor
public class UploadPreServiceImpl implements UploadPreService {


    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FileInfoDao fileInfoDao;
    @Autowired
    private final FileService fileService;

    /**
     * 校验本次文件上传请求是否合法或进行秒传操作
     *
     * @param
     * @return
     */
    @Override
    public FileValidatorVO judgeFile(String md5) {
        FileValidatorVO result = new FileValidatorVO();
        AssertUtil.isFalse(StringUtils.isBlank(md5), "file is empty");
        //MultipartFile 转化为File
        int resultType = 1;
        try {
            FileInfoDO fileInfoDO = fileInfoDao.selectByMd5(md5);

            //说明改文件在服务器中已经有备份
            if (ObjectUtil.isNotNull(fileInfoDO)) {
                //返回给前端：0不用继续操作
                resultType = 0;
                result.setFilePath(fileInfoDO.getFilePath());
            } else {
                //继续操作，上传文件，交给前端本次文件标识key
                String uploadId = fileService.requestUpload(md5);
                result.setUploadId(uploadId);
                result.setIdentifier(md5);
            }
        } catch (Exception e) {
            //如果发生转化异常，本次校验视为通过，上传文件
            logger.error(e.getMessage());
        } finally {
            result.setResponseType(resultType);
        }
        return result;
    }

    /**
     * 校验本次分片是否需要上传 如果服务端中有这个文件则跳过
     *
     * @param upFileDTO
     * @return
     */
    @Override
    public void checkFile(UpFileDTO upFileDTO) {
        UploadContext.Content content = UploadContext.get(upFileDTO.getUploadId());
        AssertUtil.isFalse(ObjectUtil.isNull(content), ResponseCode.JOB_NOE_EXIST);
        Map<Integer, PartETag> partETags =
                content.getPartETags();
        AssertUtil.isFalse(partETags.containsKey(upFileDTO.getChunkNumber()),"skip...");
    }
}
