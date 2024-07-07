package com.leyunone.disk.common.enums;

/**
 * :)
 *
 * @author LeYunone
 * @email 365627310@qq.com
 * @date 2024/7/7
 */
public enum DiskEnvEnum {

    /**
     * 网盘环境：本地  阿里云oss
     */
    LOCAL("local"),

    OSS("oss")
    ;

    private String env;

    public String getEnv() {
        return env;
    }

    public DiskEnvEnum setEnv(String env) {
        this.env = env;
        return this;
    }

    DiskEnvEnum(String env) {
        this.env = env;
    }

    public static DiskEnvEnum load(String env){
        for (DiskEnvEnum value : DiskEnvEnum.values()) {
            if(value.getEnv().equals(env)){
                return value;
            }
        }
        return OSS;
    }
}
