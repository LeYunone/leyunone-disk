package com.leyunone.disk.model;

/**
 * :)
 *
 * @Author LeYunone
 * @Date 2024/4/22 16:17
 */
public enum ResponseCode {

    SUCCESS(200, "request is success"),

    ERROR(500, "system fault"),

    FORBIDDEN(403, "forbidden access"),

    /**
     * 10001001文件
     */
    UPLOAD_FAIL(10001001,"上传失败"),
    
    FILE_NOT_EXIST(10001002,"文件不存在"),
    
    JOB_NOE_EXIST(10001003,"任务不存在"),

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
