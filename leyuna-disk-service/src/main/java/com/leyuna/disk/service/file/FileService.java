package com.leyuna.disk.service.file;

import com.leyuna.disk.DataResponse;
import com.leyuna.disk.dto.file.UpFileDTO;
import com.leyuna.disk.validator.IpValidator;
import org.springframework.stereotype.Service;

/**
 * @author pengli
 * @create 2021-12-24 16:55
 * 文件相关服务 [非查询]
 */
@Service
public class FileService {

    private IpValidator ipValidator;

    /**
     * 校验本次文件上传请求是否合法
     * @param upFileDTO
     * @return
     */
    public DataResponse JudgeFile(UpFileDTO upFileDTO){

        //首先看这个ip是否符合上传文件规则

        //判断这个文件大小 文件格式  文件名 是否合法

        //按文件名和大小 判断是否有数据一样的文件


    }
}
