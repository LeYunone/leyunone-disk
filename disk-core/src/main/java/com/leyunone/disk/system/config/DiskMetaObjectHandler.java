package com.leyunone.disk.system.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

/**
 * @author
 * @date
 */
@Configuration
public class DiskMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {

        this.setFieldValByName("updateDt", LocalDateTime.now(), metaObject);

        this.setFieldValByName("createDt", LocalDateTime.now(), metaObject);

        this.setFieldValByName("deleted", 0, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {

        this.setFieldValByName("updateDt", LocalDateTime.now(), metaObject);
    }
}
