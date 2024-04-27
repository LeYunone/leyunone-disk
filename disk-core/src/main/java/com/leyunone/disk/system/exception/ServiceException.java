package com.leyunone.disk.system.exception;

import com.leyunone.disk.model.ResponseCode;
import lombok.Getter;
import lombok.Setter;

/**
 * :)
 *
 * @Author LeYunone
 * @Date 2024/4/22 17:17
 */
@Getter
@Setter
public class ServiceException extends RuntimeException {

    private ResponseCode responseCode = ResponseCode.ERROR;

    public ServiceException(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }
}
