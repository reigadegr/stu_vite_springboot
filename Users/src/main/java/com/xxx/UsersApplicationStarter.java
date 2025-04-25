package com.xxx;

import com.xxx.config.SpringStartListener;
import lombok.val;
//import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(proxyBeanMethods = false)
//@MapperScan(basePackages = "com.xxx.mapper")
public class UsersApplicationStarter {
    public static void main(String[] args) {
        System.out.println("Hello World, UsersApplicationStarter!");
        val configurableApplicationContext =
                SpringApplication.run(UsersApplicationStarter.class, args);
        // 向Spring上下文中添加监听器
        configurableApplicationContext.addApplicationListener(new SpringStartListener());
    }
}
