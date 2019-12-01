package com.zjy.eurekaclientmovie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 电影controller
 *
 * @author zhaojianyu
 * @date 2018-11-14 8:04 PM
 */
@RestController
public class MovieController {

    private final UserClient userClient;
    private final UserClient2 userClient2;

    @Autowired
    public MovieController(UserClient userClient, UserClient2 userClient2) {
        this.userClient = userClient;
        this.userClient2 = userClient2;
    }

    @GetMapping("/movie")
    public String getUser() {
        return "result" + userClient.getUser();
    }

    @GetMapping("/movie2")
    public String getUser2() {
        return "result" + userClient2.getUser();
    }
}