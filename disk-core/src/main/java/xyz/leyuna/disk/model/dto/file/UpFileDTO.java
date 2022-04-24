package xyz.leyuna.disk.model.dto.file;

import cn.dev33.satoken.stp.SaTokenInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * @author LeYuna
 * @email 365627310@qq.com
 * @create 2021-12-24 16:51
 * 上传文件
 */
@Getter
@Setter
@ToString
public class UpFileDTO implements Serializable {

    /**
     * 上传文件人的userId
     */
    private String userId;

    private MultipartFile file;

    /**
     * 处理模式
     */
    private Integer resoleType;

    /**
     * 文件类型
     */
    private Integer fileType;

    /**
     * 文件大小 [和分片无关]
     */
    private Long fileSize;

    /**
     * 保存时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String saveTime;

    /**
     * 文件钥匙
     */
    private String fileKey;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 当前分片索引
     */
    private Integer sliceIndex;

    /**
     * 总分片数
     */
    private Integer sliceAll;
}
