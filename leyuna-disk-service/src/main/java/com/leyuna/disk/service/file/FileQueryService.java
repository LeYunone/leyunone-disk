package com.leyuna.disk.service.file;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leyuna.disk.DataResponse;
import com.leyuna.disk.co.FileInfoCO;
import com.leyuna.disk.co.FileUpLogCO;
import com.leyuna.disk.co.UserFileInfoCO;
import com.leyuna.disk.domain.FileInfoE;
import com.leyuna.disk.domain.FileUpLogE;
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

    /**
     * 分页查询文件[有排序]
     * @param file
     * @return
     */
    public DataResponse<UserFileInfoCO> selectFile(FileDTO file){
        Page<FileInfoCO> fileInfoCOPage = FileInfoE.queryInstance().getGateway().
                selectByConOrderPage(file, file.getIndex(), file.getSize(),file.getType());
        UserFileInfoCO userFileInfCO=new UserFileInfoCO();
        List<FileUpLogCO> fileUpLogCOS = FileUpLogE.queryInstance().setUserId(file.getUserId()).selectByCon();
        if(CollectionUtils.isNotEmpty(fileUpLogCOS)){
            userFileInfCO.setFileTotal(fileUpLogCOS.get(0).getUpFileTotalSize());
        }else{
            userFileInfCO.setFileTotal(0.0);
        }
        userFileInfCO.setFileinfos(fileInfoCOPage);
        return DataResponse.of(userFileInfCO);
    }

    public DataResponse<Double> selectAllFileSizeByUserId(String userId){
        List<FileUpLogCO> fileUpLogCOS =
                FileUpLogE.queryInstance().setUserId(userId).selectByCon();
        if(CollectionUtils.isEmpty(fileUpLogCOS)){
            return DataResponse.of(0.0);
        }
        return DataResponse.of(fileUpLogCOS.get(0).getUpFileTotalSize());
    }
}
