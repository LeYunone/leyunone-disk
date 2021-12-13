package com.leyuna.disk.domain;

import com.leyuna.disk.gateway.FileGateway;
import com.leyuna.disk.util.SpringContextUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * @author pengli
 * @create 2021-12-13 17:05
 */
@Getter
@Setter
public class FileE {

    public FileE(){
    }

    private FileGateway gateway;

    public FileGateway getGateway() {
        if (Objects.isNull(this.gateway)) {
            this.gateway = SpringContextUtil.getBean(FileGateway.class);
        }
        return this.gateway;
    }

    public static FileE queryInstance() {
        return new FileE();
    }

}
