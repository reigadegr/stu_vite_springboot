package com.xxx;

import com.xxx.config.SpringStartListener;
import lombok.val;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(proxyBeanMethods = false)
public class GatewayApplicationStarter {
    public static void main(String[] args) {
        System.out.println("Hello World, GatewayApplicationStarter!");
        val configurableApplicationContext =
                SpringApplication.run(GatewayApplicationStarter.class, args);
        // 向Spring上下文中添加监听器
        configurableApplicationContext.addApplicationListener(new SpringStartListener());
    }
}
