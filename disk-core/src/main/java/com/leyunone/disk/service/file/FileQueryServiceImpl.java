package com.leyunone.disk.service.file;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leyunone.disk.dao.entry.FileInfoDO;
import com.leyunone.disk.dao.repository.FileInfoDao;
import com.leyunone.disk.model.query.FileQuery;
import com.leyunone.disk.model.vo.FileInfoVO;
import com.leyunone.disk.model.vo.UserFileInfoVO;
import com.leyunone.disk.service.FileQueryService;
import com.leyunone.disk.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.leyunone.disk.model.DataResponse;
import com.leyunone.disk.common.enums.FileTypeEnum;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;


/**
 * @author LeYunone
 * @email 365627310@qq.com
 * @create 2021-12-13 10:49
 * 文件查询服务
 */
@Service
@RequiredArgsConstructor
public class FileQueryServiceImpl implements FileQueryService {

    private final FileInfoDao fileInfoDao;

    /**
     * 分页查询文件
     *
     * @param query
     * @return
     */
    @Override
    public UserFileInfoVO selectFile(FileQuery query) {
        Page<FileInfoDO> fileInfoDOPage = fileInfoDao.selectByConPage(query);
        List<FileInfoDO> records = fileInfoDOPage.getRecords();
        UserFileInfoVO userFileInfVO = new UserFileInfoVO();
        //翻译属性
        AtomicLong sum = new AtomicLong();
        List<FileInfoVO> files = records.stream().map(fileInfoDO -> {
            sum.addAndGet(fileInfoDO.getFileSize());
            FileInfoVO fileInfoVO = new FileInfoVO();
            fileInfoVO.setFileId(fileInfoDO.getFileId());
            fileInfoVO.setFileName(fileInfoDO.getFileName());
            fileInfoVO.setFileSize(FileUtil.sizeText(fileInfoDO.getFileSize()));
            fileInfoVO.setFileType(FileTypeEnum.loadName(fileInfoDO.getFileType()));
            fileInfoVO.setUpdateDt(fileInfoDO.getUpdateDt());
            fileInfoVO.setCreateDt(fileInfoDO.getCreateDt());
            return fileInfoVO;
        }).collect(Collectors.toList());
        userFileInfVO.setFileinfos(files);
        userFileInfVO.setFileTotal(FileUtil.sizeText(sum.get()));
        return userFileInfVO;
    }

}
