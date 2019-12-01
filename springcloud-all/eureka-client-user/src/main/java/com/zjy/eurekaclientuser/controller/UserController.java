package com.zjy.eurekaclientuser.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 用户controller
 *
 * @author zhaojianyu
 * @date 2018-11-14 8:04 PM
 */
@RestController
public class UserController {

    @GetMapping("/user")
    public String getUser() {
        return "我生产了用户"+new Date();
    }
}