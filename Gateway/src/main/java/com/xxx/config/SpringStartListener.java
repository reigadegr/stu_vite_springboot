package com.xxx.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class SpringStartListener implements ApplicationListener<ApplicationReadyEvent> {

    @Value("${server.port}")
    private String port;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // Spring Boot应用程序已经完全启动
        System.out.println("The Spring Boot application has fully started up!");
        System.out.println("Run on:");
        System.out.println("http://localhost:" + port);
    }
}
