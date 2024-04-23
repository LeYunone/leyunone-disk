package com.leyunone.disk.system.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * @author LeYunone
 * @email 365627310@qq.com
 * @create 2022-01-17 16:22
 *
 *  redis key过期监听事件
 */
@Configuration
public class RedisKeyExpiredListener extends KeyExpirationEventMessageListener {
    public RedisKeyExpiredListener (RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * @param message
     * @param pattern
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
    }
}
