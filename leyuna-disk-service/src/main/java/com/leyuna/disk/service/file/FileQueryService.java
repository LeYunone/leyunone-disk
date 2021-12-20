package com.leyuna.disk.service.file;

import com.leyuna.disk.DataResponse;
import com.leyuna.disk.co.FileCO;
import com.leyuna.disk.domain.FileE;
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

    public DataResponse<List<FileCO>> selectFile(FileDTO fileDTO){
        List<FileCO> fileCOS =
                FileE.of(fileDTO).selectByCon();
        return DataResponse.of(fileCOS);
    }
}
