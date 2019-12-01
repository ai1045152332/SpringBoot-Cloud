package com.zjy.eurekaclientmovie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class EurekaClientMovieApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaClientMovieApplication.class, args);
    }

}
