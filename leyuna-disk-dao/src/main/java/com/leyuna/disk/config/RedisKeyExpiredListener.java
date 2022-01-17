package com.leyuna.disk.config;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * @author pengli
 * @create 2022-01-17 16:22
 *
 *  redis key过期监听事件
 */
public class RedisKeyExpiredListener extends KeyExpirationEventMessageListener {
    public RedisKeyExpiredListener (RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        System.out.println("过期"+message.toString());
    }}
