package com.zjy.eurekaclientmovie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


/**
 * 电影controller
 *
 * @author zhaojianyu
 * @date 2018-11-14 8:04 PM
 */
@RestController
public class MovieController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/movie")
    public String getUser() {
        String url = "http://eureka-client-user/user";
        return "result" + restTemplate.getForObject(url, String.class);
    }
}