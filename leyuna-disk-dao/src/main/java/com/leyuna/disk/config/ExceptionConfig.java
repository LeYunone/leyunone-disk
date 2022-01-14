package com.leyuna.disk.config;

import com.leyuna.disk.DataResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
