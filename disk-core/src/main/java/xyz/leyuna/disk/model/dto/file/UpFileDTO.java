package xyz.leyuna.disk.model.dto.file;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * @author pengli
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
     * 多文件
     */
    private MultipartFile file;

    /**
     * 保存时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String saveTime;
}
