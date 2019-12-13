package com.zjy.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.zjy")
public class WsClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(WsClientApplication.class, args);
    }

}
