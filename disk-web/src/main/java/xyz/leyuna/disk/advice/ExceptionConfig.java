package xyz.leyuna.disk.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xyz.leyuna.disk.model.DataResponse;

/**
 * @author pengli
 * @create 2021-12-27 09:46
 *
 * 异常配置
 */
@RestControllerAdvice
public class ExceptionConfig {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(value = Exception.class)
    public DataResponse errorHandler(Exception e){
        e.printStackTrace();
        return DataResponse.buildFailure(e.getMessage());
    }
}
