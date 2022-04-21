package xyz.leyuna.disk.domain.domain;

import xyz.leyuna.disk.domain.gateway.FileUserGateway;
import xyz.leyuna.disk.model.co.FileUserCO;
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
 * (FileUser) 工作台
 *
 * @author LeYuna
 * @email 365627310@qq.com
 * @since 2022-04-21 15:51:49
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FileUserE implements Serializable {
    private static final long serialVersionUID = -26363764755947387L;

    private String id;

    private String fileId;

    private String userId;

    private Integer deleted;

    private LocalDateTime createDt;

    private LocalDateTime updateDt;

    //===========自定义方法区==========
    private FileUserGateway gateway;

    public FileUserGateway getGateway() {
        if (Objects.isNull(this.gateway)) {
            this.gateway = SpringContextUtil.getBean(FileUserGateway.class);
        }
        return this.gateway;
    }

    public static FileUserE queryInstance() {
        return new FileUserE();
    }

    public static FileUserE of(Object data) {
        return TransformationUtil.copyToDTO(data, FileUserE.class);
    }

    public List<FileUserCO> selectByCon() {
        FileUserGateway gateway = this.getGateway();
        return gateway.selectByCon(this);
    }

    public List<FileUserCO> selectByConOrder(SortEnum sort) {
        return this.getGateway().selectByConOrder(sort.getType(), this);
    }

    public boolean save() {
        FileUserGateway gateway = this.getGateway();
        return gateway.insertOrUpdate(this);
    }

    /**
     * 根据id查询
     */
    public FileUserCO selectById() {
        return this.getGateway().selectById(this.getId());
    }

    /**
     * 更新
     */
    public boolean update() {
        FileUserGateway gateway = this.getGateway();
        return gateway.update(this);
    }

    public FileUserCO selectOne() {
        FileUserGateway gateway = this.getGateway();
        return gateway.selectOne(this);
    }

    public static boolean batchCreate(List<FileUserE> list) {
        return FileUserE.queryInstance().getGateway().batchCreate(list);
    }
}
