package com.leyunone.disk.common;

import com.aliyun.oss.model.PartETag;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * :)
 * TODO 根据需求接入缓存
 *
 * @Author LeYunone
 * @Date 2024/4/25 11:22
 */
public class UploadContext {

    private final static Map<String, Content> uploadCache = new ConcurrentHashMap<>();

    private final static Map<String, String> uploadId = new ConcurrentHashMap<>();

    public static void cleanCache(String id, String md5) {
        uploadCache.remove(id);
        uploadId.remove(md5);
    }

    public static void setId(String md5, String id) {
        uploadId.put(md5, id);
    }

    public static void removeId(String md5) {
        uploadId.remove(md5);
    }

    public static String getId(String md5) {
        return uploadId.get(md5);
    }

    public static void setCache(String key, Content value) {
        uploadCache.put(key, value);
    }

    public static void removeCache(String key) {
        uploadCache.remove(key);
    }

    public static Content getUpload(String key) {
        return uploadCache.get(key);
    }

    @Getter
    @Setter
    public static class Content {

        private Map<Integer, PartETag> partETags = new ConcurrentHashMap<>();

        private Set<Integer> parts = new HashSet<>();

        /**
         * 文件标识 oss为文件名 local为文件存储目录
         */
        private String fileKey;

        private String uploadId;

        /**
         * 待上传的路径
         */
        private Set<Integer> parentIds = new ConcurrentSkipListSet<>();
    }
}
