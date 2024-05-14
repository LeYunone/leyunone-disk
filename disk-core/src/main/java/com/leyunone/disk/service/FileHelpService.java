package com.leyunone.disk.service;

import com.leyunone.disk.dao.entry.FileFolderDO;
import com.leyunone.disk.dao.entry.FileInfoDO;

/**
 * :)
 *
 * @Author pengli
 * @Date 2024/5/13 14:20
 */
public interface FileHelpService {

    FileInfoDO repeatFile(String md5, Integer tarFolderId);

    String dfsGenerateFolderPrefix(FileFolderDO fileFolderDO);
}
