package com.leyunone.disk.service.front;

import cn.hutool.core.util.ObjectUtil;
import com.aliyun.oss.model.PartETag;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.leyunone.disk.common.UploadContext;
import com.leyunone.disk.common.enums.CheckTypeEnum;
import com.leyunone.disk.dao.entry.FileFolderDO;
import com.leyunone.disk.dao.entry.FileInfoDO;
import com.leyunone.disk.dao.repository.FileFolderDao;
import com.leyunone.disk.dao.repository.FileInfoDao;
import com.leyunone.disk.model.ResponseCode;
import com.leyunone.disk.model.dto.RequestUploadDTO;
import com.leyunone.disk.model.dto.UpFileDTO;
import com.leyunone.disk.model.vo.CheckFileVO;
import com.leyunone.disk.model.vo.FileValidatorVO;
import com.leyunone.disk.service.FileHelpService;
import com.leyunone.disk.service.FileService;
import com.leyunone.disk.service.UploadPreService;
import com.leyunone.disk.util.AssertUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

    private final FileService fileService;
    private final FileHelpService fileHelpService;

    /**
     * 校验本次文件上传请求是否合法或进行秒传操作
     *
     * @param
     * @return
     */
    @Override
    public FileValidatorVO judgeFile(RequestUploadDTO requestUpload) {
        FileValidatorVO result = new FileValidatorVO();
        AssertUtil.isFalse(StringUtils.isBlank(requestUpload.getUniqueIdentifier()), "file is empty");
        //MultipartFile 转化为File
        int resultType = 1;
        FileInfoDO repeatFile = fileHelpService.repeatFile(requestUpload.getUniqueIdentifier(), requestUpload.getFolderId());

        //说明改文件在服务器中已经有备份

        if (ObjectUtil.isNotNull(repeatFile)) {
            //返回给前端：0不用继续操作
            resultType = 0;
            result.setFilePath(repeatFile.getFilePath());
        } else {
            //继续操作，上传文件，交给前端本次文件标识key
            String uploadId = fileService.requestUpload(requestUpload);
            result.setUploadId(uploadId);
        }
        result.setIdentifier(requestUpload.getUniqueIdentifier());
        result.setResponseType(resultType);
        return result;
    }

    /**
     * 校验型上传
     *
     * @param upFileDTO
     * @return
     */
    @Override
    public CheckFileVO checkFile(UpFileDTO upFileDTO) {
        UploadContext.Content content = UploadContext.getUpload(upFileDTO.getUploadId());
        AssertUtil.isFalse(ObjectUtil.isNull(content), ResponseCode.JOB_NOE_EXIST);
        CheckFileVO checkFileVO = new CheckFileVO();

        /**
         * 文件已存在
         */
        FileInfoDO repeatFile = fileHelpService.repeatFile(upFileDTO.getIdentifier(), upFileDTO.getParentId());
        checkFileVO.setSkipUpload(ObjectUtil.isNotNull(repeatFile));
        checkFileVO.setUploadedChunks(content.getPartETags().keySet());
        return checkFileVO;
    }
}
