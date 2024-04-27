package com.leyunone.disk.advice;

import com.leyunone.disk.model.DataResponse;
import com.leyunone.disk.system.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author LeYunone
 * @email 365627310@qq.com
 * @create 2021-12-27 09:46
 *
 * 异常配置
 */
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(value = ServiceException.class)
    public DataResponse<?> serviceHandler(ServiceException e){
        e.printStackTrace();
        return DataResponse.buildFailure(e.getResponseCode());
    }

    @ExceptionHandler(value = Exception.class)
    public DataResponse<?> errorHandler(Exception e){
        e.printStackTrace();
        return DataResponse.buildFailure(e.getMessage());
    }
}
