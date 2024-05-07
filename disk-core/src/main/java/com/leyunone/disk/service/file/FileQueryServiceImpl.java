package com.leyunone.disk.service.file;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leyunone.disk.dao.entry.FileFolderDO;
import com.leyunone.disk.dao.repository.FileFolderDao;
import com.leyunone.disk.model.query.FileQuery;
import com.leyunone.disk.model.vo.FileFolderVO;
import com.leyunone.disk.model.vo.SelectTreeVO;
import com.leyunone.disk.model.vo.UserFileInfoVO;
import com.leyunone.disk.service.FileQueryService;
import com.leyunone.disk.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    @Override
    public List<SelectTreeVO> getFolderTree() {
        List<SelectTreeVO> tree = new ArrayList<>();

        List<FileFolderDO> fileFolders = fileFolderDao.selectFolder();
        // 构建节点映射，方便查找父节点
        // key: folderId, value: SelectTreeVO
        Map<Integer, SelectTreeVO> nodeMap = new HashMap<>();

        // 先将所有节点转换为SelectTreeVO对象，并放入映射中
        for (FileFolderDO fileFolder : fileFolders) {
            SelectTreeVO node = new SelectTreeVO();
            node.setValue(String.valueOf(fileFolder.getFolderId()));
            node.setLabel(fileFolder.getFolderName());
            node.setChildren(new ArrayList<>());
            nodeMap.put(fileFolder.getFolderId(), node);
        }

        // 遍历节点，将节点放入父节点的children列表中
        for (FileFolderDO fileFolder : fileFolders) {
            SelectTreeVO node = nodeMap.get(fileFolder.getFolderId());
            if (fileFolder.getParentId() == null) {
                // 根节点
                tree.add(node);
            } else {
                SelectTreeVO parent = nodeMap.get(fileFolder.getParentId());
                parent.getChildren().add(node);
            }
        }
        return tree;
    }

}
