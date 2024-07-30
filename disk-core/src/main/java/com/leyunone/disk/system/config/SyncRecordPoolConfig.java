package com.leyunone.disk.system.config;

import com.leyunone.disk.system.properties.RecordPoolProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * :)
 *
 * @Author pengli
 * @Date 2024/7/29 11:12
 */
@Configuration
@EnableConfigurationProperties(RecordPoolProperties.class)
public class SyncRecordPoolConfig {

    @Bean(name = "recordPool")
    public ThreadPoolExecutor recordPool(RecordPoolProperties recordPoolProperties) {
        return new ThreadPoolExecutor(recordPoolProperties.getCorePoolSize(), recordPoolProperties.getMaxPoolSize()
                , recordPoolProperties.getAliveTime()
                , TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(recordPoolProperties.getAwaitQueue()),
                Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
    }
}
