package xyz.leyuna.disk.model.co;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * (FileInfo)出参
 *
 * @author pengli
 * @since 2022-02-10 10:19:51
 */
@Getter
@Setter
public class FileInfoCO implements Serializable {
    private static final long serialVersionUID = -33557261354917777L;

    private String id;

    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createDt;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime updateDt;

    private String saveDt;

    private Integer deleted;

    private Double fileSize;

    private String userId;

    private String fileTypeName;

    /**
     * 文件类型：1图片、2音视、3文档、4其他文件
     */
    private Integer fileType;
    
    //数组类型存储的文件
    private byte[] base64File;
}
