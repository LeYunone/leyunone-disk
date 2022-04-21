package xyz.leyuna.disk.model.dto.file;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;
import xyz.leyuna.disk.model.dto.QueryPage;

/**
 * @author pengli
 * @create 2021-12-09 16:20
 *  文件操作类
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class FileDTO extends QueryPage {

    private String id;

    private String name;

    private String userId;

    private Integer type;
    
    private MultipartFile file;

    private Integer fileType;
}
