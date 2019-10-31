package com.example.demo.annotation;

import com.example.demo.core.OperationType;

import java.lang.annotation.*;

/**
 * 操作日志注解
 *
 * @author zhaojy
 * @date 2019/10/17
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OperationLog {

    /**
     * 在方法前执行
     */
    boolean logBefore() default true;
    /**
     * 在方法后执行
     */
    boolean logAfter() default true;
    /**
     * 操作对象
     * 当前操作员的操作对象，一般为所操作记录的id。
     */
    String operateObject();

    /**
     * 当前操作所属模块
     */
    String module();

    /**
     * 操作类型
     */
    OperationType operationType();
}
