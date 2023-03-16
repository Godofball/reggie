package com.godofball.reggie;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.HandlerInterceptor;

@SpringBootApplication
@MapperScan("com.godofball.reggie.mapper")
@EnableTransactionManagement
public class ReggieApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(ReggieApplication.class, args);
    }

}
