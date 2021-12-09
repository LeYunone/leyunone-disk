package com.leyuna.disk.start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class DiskStartApplication {

    public static void main (String[] args) {
        SpringApplication.run(DiskStartApplication.class, args);
    }

}
