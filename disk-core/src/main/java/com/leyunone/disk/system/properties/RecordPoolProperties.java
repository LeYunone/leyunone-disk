package com.leyunone.disk.system.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * :)
 *
 * @Author pengli
 * @Date 2024/7/29 11:15
 */
@ConfigurationProperties(prefix = "thread.record")
public class RecordPoolProperties {

    //核心线程数
    private int corePoolSize = 2;
    //最大线程数
    private int maxPoolSize = 4;
    //空闲队列存活时间
    private int aliveTime = 30;
    //等待队列
    private int awaitQueue = 50;

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public RecordPoolProperties setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
        return this;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public RecordPoolProperties setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
        return this;
    }

    public int getAliveTime() {
        return aliveTime;
    }

    public RecordPoolProperties setAliveTime(int aliveTime) {
        this.aliveTime = aliveTime;
        return this;
    }

    public int getAwaitQueue() {
        return awaitQueue;
    }

    public RecordPoolProperties setAwaitQueue(int awaitQueue) {
        this.awaitQueue = awaitQueue;
        return this;
    }
}
