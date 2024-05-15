package com.leyunone.disk.service.file;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
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

import java.util.*;
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
        Page<FileFolderVO> fileFolderVOPage = new Page<>();
        if (StringUtils.isNotBlank(query.getNameCondition())) {
            //文件/文件夹名模糊搜索

        } else {
            fileFolderVOPage = fileFolderDao.selectPage(query);

            fileFolderVOPage.getRecords().forEach(fileFolderVO -> {
                if (StringUtils.isNotBlank(fileFolderVO.getFileSize())) {
                    fileFolderVO.setFileSize(FileUtil.sizeText(Long.parseLong(fileFolderVO.getFileSize())));
                }
            });
        }
        if (ObjectUtil.isNotNull(query.getFileFolderId())) {
            /**
             * 返回上一级
             */
            FileFolderDO fileFolderDO = fileFolderDao.selectById(query.getFileFolderId());
            if (ObjectUtil.isNotNull(fileFolderDO)) {
                FileFolderVO fileFolderVO = new FileFolderVO();
                fileFolderVO.setFolderName("../");
                fileFolderVO.setFolderId(fileFolderDO.getParentId());
                fileFolderVO.setFolder(true);

                if (CollectionUtil.isNotEmpty(fileFolderVOPage.getRecords())) {
                    fileFolderVOPage.getRecords().add(0, fileFolderVO);
                } else {
                    fileFolderVOPage.setRecords(CollectionUtil.newArrayList(fileFolderVO));
                }
            }
        }
        userFileInfVO.setInfos(fileFolderVOPage);
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
            if (fileFolder.getParentId() == null || fileFolder.getParentId() == -1) {
                // 根节点
                tree.add(node);
            } else {
                SelectTreeVO parent = nodeMap.get(fileFolder.getParentId());
                if (ObjectUtil.isNotNull(parent)) {
                    parent.getChildren().add(node);
                }
            }
        }
        return tree;
    }

    @Override
    public List<FileFolderVO> getPreFolder(Integer folderId) {
        if (ObjectUtil.isNull(folderId)) {
            return new ArrayList<>();
        }
        FileFolderDO fileFolderDO = fileFolderDao.selectById(folderId);
        if (ObjectUtil.isNull(fileFolderDO)) {
            //首页
            return new ArrayList<>();
        }
        Stack<FileFolderDO> stack = new Stack<>();
        stack.add(fileFolderDO);
        while (ObjectUtil.isNotNull(fileFolderDO.getParentId())) {
            FileFolderDO peek = stack.peek();
            if (ObjectUtil.isNotNull(peek.getParentId())) {
                FileFolderDO parentFolderDO = fileFolderDao.selectById(peek.getParentId());
                if (ObjectUtil.isNull(parentFolderDO)) break;
                stack.add(parentFolderDO);
                fileFolderDO = parentFolderDO;
            }
        }
        List<FileFolderVO> result = new ArrayList<>();
        while (!stack.empty()) {
            FileFolderDO st = stack.pop();
            FileFolderVO fileFolderVO = new FileFolderVO();
            fileFolderVO.setFolderId(st.getFolderId());
            fileFolderVO.setFolderName(st.getFolderName());
            result.add(fileFolderVO);
        }
        return result;
    }

}
