package com.leyunone.disk.common.enums;

/**
 * :)
 *
 * @Author pengli
 * @Date 2024/7/29 11:00
 */
public enum UploadStageEnum {

    /**
     * 被取消
     */
    CANCEL(-1),
    
    /**
     * 开始状态
     */
    START(0),

    /**
     * 上传中
     */
    UPLOADING(1),

    /**
     * 完成
     */
    COMPLETE(2)
    
    ;

    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public UploadStageEnum setStatus(Integer status) {
        this.status = status;
        return this;
    }

    UploadStageEnum(Integer status) {
        this.status = status;
    }
}
