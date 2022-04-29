package xyz.leyuna.disk.domain.domain;

import xyz.leyuna.disk.domain.gateway.FileInfoGateway;
import xyz.leyuna.disk.model.co.FileInfoCO;
import xyz.leyuna.disk.model.dto.file.FileDTO;
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
 * (FileInfo) 工作台
 *
 * @author pengli@asiainfo.com
 * @since 2022-04-29 11:00:31
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FileInfoE implements Serializable {
    private static final long serialVersionUID = 419928941342755505L;

    private String id;
    /**
     * 文件名
     */
    private String name;
    /**
     * 文件大小 单位为B
     */
    private Long fileSize;
    /**
     * 文件类型
     */
    private Integer fileType;
    /**
     * 保存时间
     */
    private String saveDt;

    private LocalDateTime updateDt;

    private LocalDateTime createDt;
    /**
     * 0存在 1 删除
     */
    private Integer deleted;
    /**
     * 文件路径
     */
    private String filePath;
    /**
     * 上一层父类文件夹ID
     */
    private String fileFolderId;

    //===========自定义方法区==========
    private FileInfoGateway gateway;

    public FileInfoGateway getGateway() {
        if (Objects.isNull(this.gateway)) {
            this.gateway = SpringContextUtil.getBean(FileInfoGateway.class);
        }
        return this.gateway;
    }

    public static FileInfoE queryInstance() {
        return new FileInfoE();
    }

    public static FileInfoE of(Object data) {
        return TransformationUtil.copyToDTO(data, FileInfoE.class);
    }

    public List<FileInfoCO> selectByCon() {
        FileInfoGateway gateway = this.getGateway();
        return gateway.selectByCon(this);
    }

    public List<FileInfoCO> selectByConOrder(SortEnum sort) {
        return this.getGateway().selectByConOrder(sort.getType(), this);
    }

    public String save() {
        FileInfoGateway gateway = this.getGateway();
        return gateway.save(this);
    }

    /**
     * 根据id查询
     */
    public FileInfoCO selectById() {
        return this.getGateway().selectById(this.getId());
    }

    /**
     * 更新
     */
    public boolean update() {
        FileInfoGateway gateway = this.getGateway();
        return gateway.update(this);
    }

    public FileInfoCO selectOne() {
        FileInfoGateway gateway = this.getGateway();
        return gateway.selectOne(this);
    }

    public static boolean batchCreate(List<FileInfoE> list) {
        return FileInfoE.queryInstance().getGateway().batchCreate(list);
    }

    public static List<FileInfoCO> selectInfoByUser(FileDTO fileDTO){
        return FileInfoE.queryInstance().getGateway().selectFileInfoByUser(fileDTO);
    }
}
