package com.leyuna.disk.start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages={"com.leyuna"})
@ComponentScan("com.leyuna.disk.controll")
public class DiskStartApplication {

    public static void main (String[] args) {
        SpringApplication.run(DiskStartApplication.class, args);
    }

}
