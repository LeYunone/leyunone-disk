package com.leyunone.disk.service.file;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.leyunone.disk.common.enums.FileTypeEnum;
import com.leyunone.disk.dao.entry.FileExtendContentDO;
import com.leyunone.disk.dao.entry.FileInfoDO;
import com.leyunone.disk.dao.repository.FileExtendContentDao;
import com.leyunone.disk.dao.repository.FileInfoDao;
import com.leyunone.disk.model.ResponseCode;
import com.leyunone.disk.model.bo.UploadBO;
import com.leyunone.disk.model.vo.FileInfoVO;
import com.leyunone.disk.service.FileContentService;
import com.leyunone.disk.util.AssertUtil;
import com.leyunone.disk.util.FileUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * :)
 *
 * @author LeYunone
 * @email 365627310@qq.com
 * @date 2024/7/1
 */
@Service
public class FileContentServiceImpl implements FileContentService {

    private final FileExtendContentDao fileExtendContentDao;
    private final FileInfoDao fileInfoDao;

    public FileContentServiceImpl(FileExtendContentDao fileExtendContentDao, FileInfoDao fileInfoDao) {
        this.fileExtendContentDao = fileExtendContentDao;
        this.fileInfoDao = fileInfoDao;
    }

    @Override
    public void saveFileContent(UploadBO uploadInfo) {
        String fileType = uploadInfo.getFileType();
        FileTypeEnum fileTypeEnum = FileTypeEnum.loadType(fileType);
        List<FileExtendContentDO> contents = new ArrayList<>();

        switch (fileTypeEnum) {
            case UNKNOWN:
                break;
            case FILE_WORD:
                //文档文件直接保存文本到数据库
                //TODO 需要截断
                FileExtendContentDO fileExtendContentDO = new FileExtendContentDO();
                fileExtendContentDO.setFileId(uploadInfo.getFileId());
                fileExtendContentDO.setFileContent(uploadInfo.getFileText());
                contents.add(fileExtendContentDO);
                break;
            default:
        }
        fileExtendContentDao.saveBatch(contents);
    }

    @Override
    public FileInfoVO fileDetail(String fileId) {
        FileInfoDO fileInfoDO = fileInfoDao.selectById(fileId);
        AssertUtil.isFalse(ObjectUtil.isNull(fileInfoDO), ResponseCode.FILE_NOT_EXIST);
        FileInfoVO fileInfoVO = new FileInfoVO();
        BeanUtil.copyProperties(fileInfoDO,fileInfoVO);
        String filePath = fileInfoDO.getFilePath();
        //判断文件大小进行截取内容依据
        long fileSize = fileInfoDO.getFileSize();
        FileTypeEnum fileType = FileTypeEnum.load(fileInfoDO.getFileType());
        switch (fileType) {
            case FILE_WORD:
                //文本内容
                String txtFile = FileUtil.getTxtFile(fileInfoDO.getFilePath());
                fileInfoVO.setFileContentText(txtFile);
            default:
        }
        return fileInfoVO;
    }
}
