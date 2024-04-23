package com.leyunone.disk.model;

/**
 * :)
 *
 * @Author pengli
 * @Date 2024/4/22 16:17
 */
public enum ResponseCode {

    SUCCESS(200, "request is success"),

    ERROR(500, "system fault"),

    FORBIDDEN(403, "forbidden access"),

    /**
     * 10001001文件
     */




    ;

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;

    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
