package com.leyunone.disk.service;

import com.leyunone.disk.model.query.FileQuery;
import com.leyunone.disk.model.vo.UserFileInfoVO;

/**
 * :)
 *
 * @Author pengli
 * @Date 2024/4/22 17:27
 */
public interface FileQueryService {

    UserFileInfoVO selectFile(FileQuery query);
}
