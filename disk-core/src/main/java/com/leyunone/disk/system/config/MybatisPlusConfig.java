package com.leyunone.disk.system.config;


import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author LeYunone
 * @email 365627310@qq.com
 * @date   mybatis-plus 配置类
 *  配置扫描包注释 只需要一个，在application里
 */
@Configuration
@MapperScan("com.leyunone.disk.dao.mapper")
public class MybatisPlusConfig {
    /**
     * 分页
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

}
