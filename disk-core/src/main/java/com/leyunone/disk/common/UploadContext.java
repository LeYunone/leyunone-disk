package com.leyunone.disk.common;

import com.aliyun.oss.model.PartETag;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * :)
 *
 * @Author LeYunone
 * @Date 2024/4/25 11:22
 */
public class UploadContext {

    private final static Map<String, Content> cache = new ConcurrentHashMap<>();

    public static void set(String key, Content value) {
        cache.put(key, value);
    }

    public static void remove(String key) {
        cache.remove(key);
    }

    public static Content get(String key) {
        return cache.get(key);
    }

    @Getter
    @Setter
    public static class Content {

        private Map<Integer, PartETag> partETags = new ConcurrentHashMap<>();

        /**
         * 文件标识 oss为文件名
         */
        private String fileKey;
    }
}
