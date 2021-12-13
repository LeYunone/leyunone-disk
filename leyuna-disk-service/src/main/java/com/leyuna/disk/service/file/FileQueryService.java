package com.leyuna.disk.service.file;

import com.leyuna.disk.domain.gateway.FileGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author pengli
 * @create 2021-12-13 10:49
 * 文件查询服务
 */
@Service
public class FileQueryService {

    @Autowired
    private FileGateway fileGateway;

}
