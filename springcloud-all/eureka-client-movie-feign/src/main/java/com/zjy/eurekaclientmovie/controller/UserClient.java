package com.zjy.eurekaclientmovie.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 通过url调用
 *
 * @author zhaojy
 */
@FeignClient(value = "userClient", url = "localhost:8762")
public interface UserClient {

    @GetMapping(value = "/user")
    String getUser();
}
