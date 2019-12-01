package com.zjy.eurekaclientmovie.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 通过eureka调用
 *
 * @author zhaojy
 */
@FeignClient(value = "eureka-client-user")
public interface UserClient2 {

    @GetMapping(value = "/user")
    String getUser();
}
