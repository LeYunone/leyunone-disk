package xyz.leyuna.disk.domain.domain;

import xyz.leyuna.disk.domain.gateway.FileUpLogGateway;
import xyz.leyuna.disk.model.co.FileUpLogCO;
import xyz.leyuna.disk.model.enums.SortEnum;
import xyz.leyuna.disk.util.SpringContextUtil;
import xyz.leyuna.disk.util.TransformationUtil;
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
 * @since 2022-01-18 15:24:11
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FileUpLogE implements Serializable {
    private static final long serialVersionUID = -72076318261188848L;

    private String id;
    /**
     * 用户
     */
    private String userId;
    /**
     * 更新时间
     */
    private LocalDateTime updateDt;
    /**
     * 创建时间
     */
    private LocalDateTime createDt;
    /**
     * 最后一次上传的合法标志
     */
    private Integer upSign;
    /**
     * 当前操作后的文件总内存
     */
    private Double upFileTotalSize;

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
