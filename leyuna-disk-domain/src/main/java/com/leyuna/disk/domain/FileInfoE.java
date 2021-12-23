package com.leyuna.disk.domain;

import com.leyuna.disk.co.FileInfoCO;
import com.leyuna.disk.gateway.FileInfoGateway;
import com.leyuna.disk.util.SpringContextUtil;
import com.leyuna.disk.util.TransformationUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * (FileInfo) 工作台
 *
 * @author pengli
 * @since 2021-12-21 16:08:24
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FileInfoE implements Serializable {
    private static final long serialVersionUID = 176152346728039134L;

    private String id;

    private String name;

    private LocalDateTime createDt;

    private LocalDateTime updateDt;

    private Integer deleted;

    //===========自定义方法区==========
    private FileInfoGateway gateway;

    public FileInfoGateway getGateway () {
        if (Objects.isNull(this.gateway)) {
            this.gateway = SpringContextUtil.getBean(FileInfoGateway.class);
        }
        return this.gateway;
    }

    public static FileInfoE queryInstance () {
        return new FileInfoE();
    }

    public static FileInfoE of (Object data) {
        return TransformationUtil.copyToDTO(data, FileInfoE.class);
    }

    public List<FileInfoCO> selectByCon () {
        FileInfoGateway gateway = this.getGateway();
        return gateway.selectByCon(this);
    }

    public boolean save () {
        FileInfoGateway gateway = this.getGateway();
        return gateway.insertOrUpdate(this);
    }

    /**
     * 根据id查询
     */
    public FileInfoCO selectById () {
        return this.getGateway().selectById(this.getId());
    }

    /**
     * 更新
     */
    public boolean update () {
        FileInfoGateway gateway = this.getGateway();
        return gateway.update(this);
    }

    public static boolean batchCreate (List<FileInfoE> list) {
        return FileInfoE.queryInstance().getGateway().batchCreate(list);
    }
}
