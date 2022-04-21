package xyz.leyuna.disk.config;

import com.alibaba.cloud.nacos.registry.NacosAutoServiceRegistration;
import com.alibaba.cloud.nacos.registry.NacosRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author pengli
 * @create 2022-02-17 10:23
 */
@Component
public class NacosListener implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    private NacosRegistration registration;

    @Autowired
    private NacosAutoServiceRegistration nacosAutoServiceRegistration;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        String property = event.getApplicationContext().getEnvironment().getProperty("server.port");
        registration.setPort(Integer.valueOf(property));
        nacosAutoServiceRegistration.start();
    }
    @EventListener(ApplicationReadyEvent.class)
    public void onWebServerReady(ApplicationReadyEvent event) {
        String property = event.getApplicationContext().getEnvironment().getProperty("server.port");
        registration.setPort(Integer.valueOf(property));
        nacosAutoServiceRegistration.start();
    }
}