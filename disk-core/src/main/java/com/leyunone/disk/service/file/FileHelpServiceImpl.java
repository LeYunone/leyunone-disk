package com.leyunone.disk.service.file;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.leyunone.disk.dao.entry.FileFolderDO;
import com.leyunone.disk.dao.entry.FileInfoDO;
import com.leyunone.disk.dao.repository.FileFolderDao;
import com.leyunone.disk.dao.repository.FileInfoDao;
import com.leyunone.disk.model.ResponseCode;
import com.leyunone.disk.service.FileHelpService;
import com.leyunone.disk.util.AssertUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Stack;
import java.util.stream.Collectors;

/**
 * :)
 *
 * @Author pengli
 * @Date 2024/5/13 14:20
 */
@Service
@RequiredArgsConstructor
public class FileHelpServiceImpl implements FileHelpService {

    private final FileInfoDao fileInfoDao;
    private final FileFolderDao fileFolderDao;


    /**
     * 重复文件判断和处理
     *
     * @param md5
     * @param tarFolderId 目标目录id
     * @return
     */
    @Override
    public FileInfoDO repeatFile(String md5, Integer tarFolderId) {
        FileInfoDO fileInfoDO = fileInfoDao.selectByMd5(md5);
        if (ObjectUtil.isNotNull(fileInfoDO)) {
            /**
             * 只生成本次的指向文件
             */
            FileFolderDO existFileFolder = fileFolderDao.selectByFileIdParentId(fileInfoDO.getFileId(), tarFolderId);
            AssertUtil.isFalse(ObjectUtil.isNotNull(existFileFolder), ResponseCode.FILE_EXIST);
            FileFolderDO fileFolderDO = new FileFolderDO();
            fileFolderDO.setFolder(false);
            fileFolderDO.setParentId(tarFolderId);
            fileFolderDO.setFileId(fileInfoDO.getFileId());
            fileFolderDao.save(fileFolderDO);
        }
        return fileInfoDO;
    }

    /**
     * 根据当前文件夹生成前缀
     * @param fileFolderDO
     * @return
     */
    @Override
    public String dfsGenerateFolderPrefix(FileFolderDO fileFolderDO) {
        Stack<FileFolderDO> stack = new Stack<>();
        stack.add(fileFolderDO);
        while (true) {
            FileFolderDO poll = stack.peek();
            if (ObjectUtil.isNotNull(poll)) {
                if (ObjectUtil.isNotNull(poll.getParentId())) {
                    FileFolderDO parentFolder = fileFolderDao.selectById(poll.getParentId());
                    stack.add(parentFolder);
                } else {
                    break;
                }
            }
            break;
        }
        String perfix = "";
        if (CollectionUtil.isNotEmpty(stack)) {
            perfix = CollectionUtil.join(stack.stream().filter(f -> ObjectUtil.isNotNull(f) && StringUtils.isNotBlank(f.getFolderName())).map(FileFolderDO::getFolderName).collect(Collectors.toList()), "");
        }
        return perfix;
    }
}
