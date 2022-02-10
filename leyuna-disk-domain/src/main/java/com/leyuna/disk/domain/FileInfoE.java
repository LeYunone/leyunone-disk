package com.leyuna.disk.domain;

import com.leyuna.disk.co.FileInfoCO;
import com.leyuna.disk.enums.SortEnum;
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
 * @since 2021-12-28 09:45:13
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FileInfoE implements Serializable {
    private static final long serialVersionUID = -86181643445529472L;

    private String id;

    private String name;

    private LocalDateTime createDt;

    private LocalDateTime updateDt;

    private Integer deleted;

    private Double fileSize;

    private String userId;

    private Integer fileType;

    private String fileTypeName;

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

    public List<FileInfoCO> selectByConOrder (SortEnum sort) {
        return this.getGateway().selectByConOrder(sort.getType(), this);
    }

    public String save () {
        FileInfoGateway gateway = this.getGateway();
        return gateway.save(this);
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
