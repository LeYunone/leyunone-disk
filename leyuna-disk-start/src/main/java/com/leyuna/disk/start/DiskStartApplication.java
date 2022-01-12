package com.leyuna.disk.start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication()
@ComponentScan({"com.leyuna.disk"})
@EnableCaching //开启缓存
@EnableDiscoveryClient
public class DiskStartApplication {

    public static void main (String[] args) {
        SpringApplication.run(DiskStartApplication.class, args);
    }

}
