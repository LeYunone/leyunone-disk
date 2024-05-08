package com.leyunone.disk.service;

import com.leyunone.disk.model.query.FileQuery;
import com.leyunone.disk.model.vo.FileFolderVO;
import com.leyunone.disk.model.vo.SelectTreeVO;
import com.leyunone.disk.model.vo.UserFileInfoVO;

import java.util.List;

/**
 * :)
 *
 * @Author LeYunone
 * @Date 2024/4/22 17:27
 */
public interface FileQueryService {

    UserFileInfoVO getFiles(FileQuery query);

    List<SelectTreeVO> getFolderTree();

    List<FileFolderVO> getPreFolder(Integer folderId);
}
