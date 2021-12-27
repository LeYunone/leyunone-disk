package com.leyuna.disk.domain;

import com.leyuna.disk.co.FileUpLogCO;
import com.leyuna.disk.enums.SortEnum;
import com.leyuna.disk.gateway.FileUpLogGateway;
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
 * (FileUpLog) 工作台
 *
 * @author pengli
 * @since 2021-12-27 15:02:00
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FileUpLogE implements Serializable {
    private static final long serialVersionUID = -25693128206428474L;

    private String id;

    private String userId;

    private LocalDateTime updateDt;

    private LocalDateTime createDt;
    /**
     * 最后一次上传的合法标志
     */
    private Integer upSign;

    //===========自定义方法区==========
    private FileUpLogGateway gateway;

    public FileUpLogGateway getGateway () {
        if (Objects.isNull(this.gateway)) {
            this.gateway = SpringContextUtil.getBean(FileUpLogGateway.class);
        }
        return this.gateway;
    }

    public static FileUpLogE queryInstance () {
        return new FileUpLogE();
    }

    public static FileUpLogE of (Object data) {
        return TransformationUtil.copyToDTO(data, FileUpLogE.class);
    }

    public List<FileUpLogCO> selectByCon () {
        FileUpLogGateway gateway = this.getGateway();
        return gateway.selectByCon(this);
    }

    public List<FileUpLogCO> selectByConOrder (SortEnum sort) {
        return this.getGateway().selectByConOrder(sort.getType(), this);
    }

    public boolean save () {
        FileUpLogGateway gateway = this.getGateway();
        return gateway.insertOrUpdate(this);
    }

    /**
     * 根据id查询
     */
    public FileUpLogCO selectById () {
        return this.getGateway().selectById(this.getId());
    }

    /**
     * 更新
     */
    public boolean update () {
        FileUpLogGateway gateway = this.getGateway();
        return gateway.update(this);
    }

    public static boolean batchCreate (List<FileUpLogE> list) {
        return FileUpLogE.queryInstance().getGateway().batchCreate(list);
    }
}
