package com.leyuna.disk.domain;

import com.leyuna.disk.co.FileCO;
import com.leyuna.disk.dto.file.FileDTO;
import com.leyuna.disk.gateway.FileGateway;
import com.leyuna.disk.util.SpringContextUtil;
import com.leyuna.disk.util.TransformationUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

/**
 * @author pengli
 * @create 2021-12-13 17:05
 */
@Getter
@Setter
public class FileE {

    private String id;

    public FileE(){
    }

    private FileGateway gateway;

    private FileGateway getGateway() {
        if (Objects.isNull(this.gateway)) {
            this.gateway = SpringContextUtil.getBean(FileGateway.class);
        }
        return this.gateway;
    }

    public static FileE of(FileDTO fileDTO){
        return TransformationUtil.copyToDTO(fileDTO,FileE.class);
    }

    public static FileE queryInstance() {
        return new FileE();
    }

    public List<FileCO> selectByCon(){
        List<FileCO> fileCOS = this.getGateway().selectByCon(this);
        return fileCOS;
    }

    public FileCO selectById(){
        FileCO fileCO = this.getGateway().selectById(this.id);
        return fileCO;
    }
}
