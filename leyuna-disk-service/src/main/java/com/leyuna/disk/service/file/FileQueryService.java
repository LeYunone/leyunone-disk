package com.leyuna.disk.service.file;

import com.leyuna.disk.DataResponse;
import com.leyuna.disk.co.FileInfoCO;
import com.leyuna.disk.domain.FileInfoE;
import com.leyuna.disk.dto.file.FileDTO;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author pengli
 * @create 2021-12-13 10:49
 * 文件查询服务
 */
@Service
public class FileQueryService {

    public DataResponse<List<FileInfoCO>> selectFile(String id){
        List<FileInfoCO> fileInfoCOS = FileInfoE.queryInstance().setId(id).selectByCon();
        return DataResponse.of(fileInfoCOS);
    }
}
