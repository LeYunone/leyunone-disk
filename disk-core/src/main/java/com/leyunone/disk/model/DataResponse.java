package com.leyunone.disk.model;

import lombok.*;

/**
 * @author LeYunone
 * @email 365627310@qq.com
 * @create 2021-08-10 15:05
 * 一般响应结果集
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class DataResponse<T> {

    private T result;

    private boolean success;

    private String message;

    private int code;

    public DataResponse() {
    }

    public static <T> DataResponse<T> of(T data) {
        DataResponse<T> response = new DataResponse();
        response.setSuccess(true);
        response.setResult(data);
        response.setCode(ResponseCode.SUCCESS.getCode());
        return response;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public static <T> DataResponse<T> of(boolean status, ResponseCode code, T data) {
        DataResponse<T> response = new DataResponse<>();
        response.setSuccess(status);
        response.setResult(data);
        return response;
    }

    public static <T> DataResponse<T> of() {
        DataResponse<T> response = new DataResponse<>();
        response.setSuccess(true);
        return response;
    }

    public static DataResponse<?> buildFailure(ResponseCode responseCode) {
        return of(false, responseCode, (Object) null);
    }

    public static DataResponse<?> buildFailure() {
        return of(false, ResponseCode.ERROR, (Object) null);
    }

    public static DataResponse<?> buildFailure(String message) {
        DataResponse<?> response = of(false, ResponseCode.ERROR, (Object) null);
        response.setMessage(message);
        return response;
    }

}
