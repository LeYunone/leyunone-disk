package com.leyunone.disk.service.front;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.leyunone.disk.dao.entry.FileInfoDO;
import com.leyunone.disk.dao.entry.FileMd5DO;
import com.leyunone.disk.dao.repository.FileInfoDao;
import com.leyunone.disk.dao.repository.FileMd5Dao;
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

/**
 * :)
 *
 * @Author pengli
 * @Date 2024/4/22 17:12
 */
@Service
@RequiredArgsConstructor
public class UploadPreServiceImpl implements UploadPreService {


    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FileMd5Dao fileMd5Dao;
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
            FileMd5DO fileMd5DO = fileMd5Dao.selectById(md5);

            //说明改文件在服务器中已经有备份
            if (ObjectUtil.isNotNull(fileMd5DO)) {
                FileInfoDO fileInfoDO = fileInfoDao.selectById(fileMd5DO.getFileId());
                //返回给前端：0不用继续操作
                if (ObjectUtil.isNotNull(fileInfoDO)) {
                    resultType = 0;
                    result.setFilePath(fileInfoDO.getFilePath());
                }
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

//        String fileMD5Value = upFileDTO.getIdentifier();
//
//        AssertUtil.isFalse(StrUtil.isBlank(fileMD5Value), ErrorEnum.FILE_UPLOAD_FILE.getName());
//        //获得分片文件存储的临时目录
//        String tempPath = FileUtil.resoleFileTempPath(fileMD5Value);
//        AssertUtil.isFalse(StrUtil.isBlank(tempPath), ErrorEnum.FILE_UPLOAD_FILE.getName());
//
//        //开始进行切片化上传
//        File sliceFile = new File(tempPath + upFileDTO.getChunkNumber());
//        AssertUtil.isFalse(sliceFile.exists(), ErrorEnum.FILE_UPLOAD_FILE.getName());
    }
}
