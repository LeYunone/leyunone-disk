package com.leyunone.disk.util;

import cn.hutool.core.util.ObjectUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * :)
 * 业务id锁
 *
 * @Author pengli
 * @Date 2024/7/29 14:45
 */
public class BusinessIdLock {

    /**
     * 10秒内没有锁竞争或业务代码执行超过10秒，丢掉锁
     */
    private static final Cache<Object, ReentrantLock> LOCK_STORAGE = CacheBuilder.newBuilder().expireAfterAccess(10000, TimeUnit.MILLISECONDS).build();

    public static void lock(Object businessId) {
        ReentrantLock reentrantLock = null;
        try {
            reentrantLock = LOCK_STORAGE.get(businessId, ReentrantLock::new);
            reentrantLock.lock();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            if (ObjectUtil.isNotNull(reentrantLock)) {
                reentrantLock.unlock();
            }
        }
    }

    public static void unLock(Object businessId) {
        ReentrantLock reentrantLock = LOCK_STORAGE.getIfPresent(businessId);
        if (ObjectUtil.isNotNull(reentrantLock)) {
            reentrantLock.unlock();
        }
    }
}
