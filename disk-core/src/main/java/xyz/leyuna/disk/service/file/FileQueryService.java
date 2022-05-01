package xyz.leyuna.disk.service.file;

import cn.hutool.core.util.ObjectUtil;
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
     *
     * @param file
     * @return
     */
    public DataResponse<UserFileInfoCO> selectFile(FileDTO file) {
        List<FileInfoCO> fileInfoCOS = FileInfoE.selectInfoByUser(file);
        UserFileInfoCO userFileInfCO = new UserFileInfoCO();
        FileUpLogCO fileUpLogCO = FileUpLogE.queryInstance().setUserId(file.getUserId()).selectOne();
        if (ObjectUtil.isNotEmpty(fileUpLogCO)) {
            userFileInfCO.setFileTotal(fileUpLogCO.getUpFileTotalSize());
        } else {
            userFileInfCO.setFileTotal(0L);
        }
        //翻译属性
        fileInfoCOS.stream().forEach(co -> {
            if (null != co.getFileType()) {
                co.setFileTypeText(FileEnum.loadName(co.getFileType()).getName());
            }
            Long fileSize = co.getFileSize();
            if (null != fileSize) {
                if (fileSize < 1024) {
                    co.setFileSizeText(fileSize + "B");
                } else if (fileSize < 1048576) {
                    co.setFileSizeText(String.format("%.2f", fileSize / 1024.0) + "KB");
                } else if (fileSize < 1073741824) {
                    co.setFileSizeText(String.format("%.2f", fileSize / (1024 * 1024.0)) + "MB");
                } else {
                    co.setFileSizeText(String.format("%.2f", fileSize / (1024 * 1024 * 1024.0)) + "G");
                }
            }
        });
        userFileInfCO.setFileinfos(fileInfoCOS);
        return DataResponse.of(userFileInfCO);
    }

    public DataResponse<Long> selectAllFileSizeByUserId(String userId) {
        List<FileUpLogCO> fileUpLogCOS =
                FileUpLogE.queryInstance().setUserId(userId).selectByCon();
        if (CollectionUtils.isEmpty(fileUpLogCOS)) {
            return DataResponse.of(0L);
        }
        return DataResponse.of(fileUpLogCOS.get(0).getUpFileTotalSize());
    }
}
