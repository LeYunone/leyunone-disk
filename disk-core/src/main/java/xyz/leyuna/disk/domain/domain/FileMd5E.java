package xyz.leyuna.disk.domain.domain;

import xyz.leyuna.disk.domain.gateway.FileMd5Gateway;
import xyz.leyuna.disk.model.co.FileMd5CO;
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
 * (FileMd5) 工作台
 *
 * @author LeYuna
 * @email 365627310@qq.com
 * @since 2022-04-21 15:51:42
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FileMd5E implements Serializable {
    private static final long serialVersionUID = 659929498245395458L;

    private String id;

    private String fileId;
    /**
     * 文件MD5编码
     */
    private String md5Code;
    /**
     * 存在状态 0 1
     */
    private Integer delete;

    private LocalDateTime createDt;

    private LocalDateTime updateDt;

    //===========自定义方法区==========
    private FileMd5Gateway gateway;

    public FileMd5Gateway getGateway() {
        if (Objects.isNull(this.gateway)) {
            this.gateway = SpringContextUtil.getBean(FileMd5Gateway.class);
        }
        return this.gateway;
    }

    public static FileMd5E queryInstance() {
        return new FileMd5E();
    }

    public static FileMd5E of(Object data) {
        return TransformationUtil.copyToDTO(data, FileMd5E.class);
    }

    public List<FileMd5CO> selectByCon() {
        FileMd5Gateway gateway = this.getGateway();
        return gateway.selectByCon(this);
    }

    public List<FileMd5CO> selectByConOrder(SortEnum sort) {
        return this.getGateway().selectByConOrder(sort.getType(), this);
    }

    public boolean save() {
        FileMd5Gateway gateway = this.getGateway();
        return gateway.insertOrUpdate(this);
    }

    /**
     * 根据id查询
     */
    public FileMd5CO selectById() {
        return this.getGateway().selectById(this.getId());
    }

    /**
     * 更新
     */
    public boolean update() {
        FileMd5Gateway gateway = this.getGateway();
        return gateway.update(this);
    }

    public FileMd5CO selectOne() {
        FileMd5Gateway gateway = this.getGateway();
        return gateway.selectOne(this);
    }

    public static boolean batchCreate(List<FileMd5E> list) {
        return FileMd5E.queryInstance().getGateway().batchCreate(list);
    }
}
