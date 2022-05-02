package xyz.leyuna.disk.model.dto.file;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Transient;
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

    /**
     * 当前分片下标
     */
    private Integer chunkNumber;

    /**
     * 分片大小
     */
    private Integer chunkSize;

    /**
     * 当前分片大小
     */
    private Integer currentChunkSize;

    /**
     * 总文件大小
     */
    private Long totalSize;

    /**
     * 文件唯一标识
     */
    private String identifier;

    /**
     * 文件名
     */
    private String filename;

    /**
     * 总分片数
     */
    private Integer totalChunks;

    @Transient
    private MultipartFile file;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     *
     */
    private String type;

    /**
     * 保存时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String saveTime;

    /**
     * 上一层文件夹id
     */
    private String fileFolderId;

    private String uniqueIdentifier;
}
