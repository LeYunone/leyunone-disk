package xyz.leyuna.disk.service.file;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import xyz.leyuna.disk.domain.domain.FileInfoE;
import xyz.leyuna.disk.domain.domain.FileUpLogE;
import xyz.leyuna.disk.model.DataResponse;
import xyz.leyuna.disk.model.co.FileInfoCO;
import xyz.leyuna.disk.model.co.FileUpLogCO;
import xyz.leyuna.disk.model.co.UserFileInfoCO;
import xyz.leyuna.disk.model.dto.file.FileDTO;
import xyz.leyuna.disk.model.enums.FileEnum;

import java.util.List;


/**
 * @author LeYuna
 * @email 365627310@qq.com
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
            userFileInfCO.setFileTotal(0L);
        }
        //翻译文件类型
        fileInfoCOPage.getRecords().stream().forEach(co -> {
            co.setFileTypeText(FileEnum.loadName(co.getFileType()).getName());
        });
        userFileInfCO.setFileinfos(fileInfoCOPage);
        return DataResponse.of(userFileInfCO);
    }

    public DataResponse<Long> selectAllFileSizeByUserId(String userId){
        List<FileUpLogCO> fileUpLogCOS =
                FileUpLogE.queryInstance().setUserId(userId).selectByCon();
        if(CollectionUtils.isEmpty(fileUpLogCOS)){
            return DataResponse.of(0L);
        }
        return DataResponse.of(fileUpLogCOS.get(0).getUpFileTotalSize());
    }
}
