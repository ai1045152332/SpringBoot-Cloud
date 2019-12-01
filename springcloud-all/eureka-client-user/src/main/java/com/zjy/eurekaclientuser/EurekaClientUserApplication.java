package com.zjy.eurekaclientuser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
/**
 * eureka 客户端 生产者
 *
 * @author zhaojy
 * @date 19/12/1
 */
@EnableEurekaClient
@SpringBootApplication
public class EurekaClientUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaClientUserApplication.class, args);
    }

}
