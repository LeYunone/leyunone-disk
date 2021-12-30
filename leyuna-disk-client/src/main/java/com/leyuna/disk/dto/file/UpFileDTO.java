package com.leyuna.disk.dto.file;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

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
    private List<MultipartFile> files;

    /**
     * 保存时间
     */
    private LocalDateTime saveTime;
}
