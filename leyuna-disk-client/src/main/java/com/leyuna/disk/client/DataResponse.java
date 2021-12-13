package com.leyuna.disk.client;

import lombok.Getter;
import lombok.Setter;

/**
 * @author pengli
 * @create 2021-12-09 11:02
 */
@Getter
@Setter
public class DataResponse<T> {

    private T data;

    private String code;

    private boolean status;


    public static <T>DataResponse<T> of(T data){
        DataResponse<T> res=new DataResponse<>();
        res.setStatus(true);
        res.setData(data);
        return res;
    }
}
