package xyz.leyuna.disk.service.file;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xyz.leyuna.disk.domain.domain.FileInfoE;
import xyz.leyuna.disk.model.DataResponse;
import xyz.leyuna.disk.model.co.FileInfoCO;
import xyz.leyuna.disk.model.constant.ServerCode;
import xyz.leyuna.disk.model.dto.file.UpFileDTO;
import xyz.leyuna.disk.util.FileUtil;
import xyz.leyuna.disk.validator.FileValidator;
import xyz.leyuna.disk.validator.UserValidator;

import java.io.File;
import java.util.List;

/**
 * @author pengli
 * @date 2022-04-21
 */
@Service
public class ValidatorService {

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private FileValidator fileValidator;

    /**
     * 校验本次文件上传请求是否合法
     *
     * @param upFileDTO
     * @return
     */
    public DataResponse<Integer> judgeFile (UpFileDTO upFileDTO) {
        //首先看这个用户是否符合上传文件规则
        userValidator.validator(upFileDTO.getUserId());
        
        MultipartFile file = upFileDTO.getFile();
        if (ObjectUtil.isEmpty(file)) {
            return DataResponse.buildFailure();
        }
        double fileSize=(double)file.getSize()/1024;
        String fileName = file.getOriginalFilename();
        //名称校验
        fileValidator.validator(fileName, fileSize, null);
        //按文件名和大小 判断是否有数据一样的文件
        List<FileInfoCO> fileInfoCOS = FileInfoE.queryInstance().setName(fileName).setFileSize(fileSize).selectByCon();

        if(CollectionUtil.isNotEmpty(fileInfoCOS)){
            FileInfoCO fileInfoCO = fileInfoCOS.get(0);
            //如果这个文件是我自己硬盘里的文件则跳过
            if(!fileInfoCO.getUserId().equals(upFileDTO.getUserId())){
                //如果服务器中有一个一模一样的文件，则直接进行拷贝操作
                String path="/"+fileInfoCO.getFileType()+"/"+fileInfoCO.getName();
                File copyFile=new File(ServerCode.FILE_ADDRESS+fileInfoCO.getUserId()
                        +path);

                FileUtil.copyFile(copyFile, upFileDTO.getUserId()+path);
            }
            //返回一个空对象
            return DataResponse.of(0);
        }
        return DataResponse.of(1);
    }
}
