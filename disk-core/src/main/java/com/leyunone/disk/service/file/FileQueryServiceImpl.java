package com.leyunone.disk.service.file;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leyunone.disk.dao.entry.FileFolderDO;
import com.leyunone.disk.dao.repository.FileFolderDao;
import com.leyunone.disk.model.query.FileQuery;
import com.leyunone.disk.model.vo.FileFolderVO;
import com.leyunone.disk.model.vo.UserFileInfoVO;
import com.leyunone.disk.service.FileQueryService;
import com.leyunone.disk.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



/**
 * @author LeYunone
 * @email 365627310@qq.com
 * @create 2021-12-13 10:49
 * 文件查询服务
 */
@Service
@RequiredArgsConstructor
public class FileQueryServiceImpl implements FileQueryService {

    private final FileFolderDao fileFolderDao;

    /**
     * 分页查询文件
     *
     * @param query
     * @return
     */
    @Override
    public UserFileInfoVO getFiles(FileQuery query) {
        UserFileInfoVO userFileInfVO = new UserFileInfoVO();

        if (StringUtils.isNotBlank(query.getNameCondition())) {
            //文件/文件夹名前缀模糊搜索

        } else {
            Page<FileFolderVO> fileFolderVOPage = fileFolderDao.selectPage(query);

            fileFolderVOPage.getRecords().forEach(fileFolderVO -> {
                if (StringUtils.isNotBlank(fileFolderVO.getFileSize())) {
                    fileFolderVO.setFileSize(FileUtil.sizeText(Long.parseLong(fileFolderVO.getFileSize())));
                }
            });
            userFileInfVO.setInfos(fileFolderVOPage);
        }
        return userFileInfVO;
    }

}
