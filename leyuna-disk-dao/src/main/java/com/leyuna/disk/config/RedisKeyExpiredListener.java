package com.leyuna.disk.config;

import com.leyuna.disk.co.FileInfoCO;
import com.leyuna.disk.domain.FileInfoE;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * @author pengli
 * @create 2022-01-17 16:22
 *
 *  redis key过期监听事件
 */
@Configuration
public class RedisKeyExpiredListener extends KeyExpirationEventMessageListener {
    public RedisKeyExpiredListener (RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    /**
     * 云盘内处理过期事件 ： 暂时保存文件
     * @param message
     * @param pattern
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String fileId = message.toString().substring(5, message.toString().length());
        //如果在缓存里 只需要计算数据库
        FileInfoCO fileInfoCO = FileInfoE.queryInstance().setId(fileId).selectById();
        //半永久则需要进行硬盘中删除文件

        System.out.println(substring);
    }}
