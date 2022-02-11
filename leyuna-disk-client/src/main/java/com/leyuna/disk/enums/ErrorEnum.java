package com.leyuna.disk.enums;

/**
 * @author pengli
 * @create 2021-12-27 10:03
 */
public enum  ErrorEnum {

    USER_BLACK("操作失败：用户已进黑名单，请联系站长","1"),
    USER_UPLOAD_FILE("操作失败：上传失败[内存已满或是用户异常]，请联系站长","2"),
    USER_INFO_ERROR("操作失败：用户信息错误，不一致","8"),

    FILE_NAME_EMPTY("操作失败：请检测文件名是否为空","3"),
    FILE_UPLOAD_FILE("操作失败：服务器上传文件异常，请联系站长","4"),
    FILE_USER_OVER("操作失败：用户文件内存已满，请清理掉无用文件释放空间","5"),

    SERVER_ERROR("操作失败：服务器异常","6"),

    SELECT_NOT_FOUND("操作失败：相关信息已丢失,请联系站长","7");


    private String name;

    private String value;

    ErrorEnum(String name,String value){
        this.name=name;
        this.value=value;
    }

    public String getName(){
        return this.name;
    }

    public String getValue(){
        return this.value;
    }
}
