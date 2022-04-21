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
 * @author LeYuna
 * @email 365627310@qq.com
 * @since 2022-04-21 15:51:45
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FileUpLogE implements Serializable {
    private static final long serialVersionUID = -94808602625431516L;

    private String id;

    private String userId;

    private Integer upSign;

    private Long upFileTotalSize;

    private LocalDateTime updateDt;

    private LocalDateTime createDt;

    //===========自定义方法区==========
    private FileUpLogGateway gateway;

    public FileUpLogGateway getGateway() {
        if (Objects.isNull(this.gateway)) {
            this.gateway = SpringContextUtil.getBean(FileUpLogGateway.class);
        }
        return this.gateway;
    }

    public static FileUpLogE queryInstance() {
        return new FileUpLogE();
    }

    public static FileUpLogE of(Object data) {
        return TransformationUtil.copyToDTO(data, FileUpLogE.class);
    }

    public List<FileUpLogCO> selectByCon() {
        FileUpLogGateway gateway = this.getGateway();
        return gateway.selectByCon(this);
    }

    public List<FileUpLogCO> selectByConOrder(SortEnum sort) {
        return this.getGateway().selectByConOrder(sort.getType(), this);
    }

    public boolean save() {
        FileUpLogGateway gateway = this.getGateway();
        return gateway.insertOrUpdate(this);
    }

    /**
     * 根据id查询
     */
    public FileUpLogCO selectById() {
        return this.getGateway().selectById(this.getId());
    }

    /**
     * 更新
     */
    public boolean update() {
        FileUpLogGateway gateway = this.getGateway();
        return gateway.update(this);
    }

    public FileUpLogCO selectOne() {
        FileUpLogGateway gateway = this.getGateway();
        return gateway.selectOne(this);
    }

    public static boolean batchCreate(List<FileUpLogE> list) {
        return FileUpLogE.queryInstance().getGateway().batchCreate(list);
    }
}
