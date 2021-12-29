package com.leyuna.disk.service.file;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leyuna.disk.DataResponse;
import com.leyuna.disk.co.FileInfoCO;
import com.leyuna.disk.domain.FileInfoE;
import com.leyuna.disk.dto.file.FileDTO;
import com.leyuna.disk.util.TransformationUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author pengli
 * @create 2021-12-13 10:49
 * 文件查询服务
 */
@Service
public class FileQueryService {

    /**
     * 分页查询文件[有排序]
     * @param file
     * @return
     */
    public DataResponse<Page<FileInfoCO>> selectFile(FileDTO file){
        Page<FileInfoCO> fileInfoCOPage = FileInfoE.queryInstance().getGateway().
                selectByConOrderPage(TransformationUtil.copyToDTO(file,FileInfoE.class), file.getIndex(), file.getSize(),file.getType());
        return DataResponse.of(fileInfoCOPage);
    }
}
