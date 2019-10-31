package com.example.demo;

import com.example.demo.annotation.OperationLog;
import com.example.demo.core.OperationType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author zjy
 * @date
 *
 */
@RestController
public class HelloController {

    @GetMapping("/hello")
    @OperationLog(operateObject="aaa",module="bbb",operationType= OperationType.CREATE)
    public String hello(@RequestParam String hello){
        return "hello";
    }

    @GetMapping("/hello2")
    @OperationLog(operateObject="aaa",module="bbb",operationType= OperationType.CREATE)
    public String hello2(@RequestParam String hello){
        int a= 1/0;
        return "1";
    }
}
