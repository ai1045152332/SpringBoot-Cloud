package com.example.demo.model;

import lombok.Data;

/**
 * 操作日志注解
 *
 * @author zhaojy
 * @date 2019/10/17
 */
@Data
public class OperationLogEntity {

    /**
     * 账户登录名
     */
    private String accountLoginName;

    /**
     * 操作对象
     */
    private String operateObject;

    /**
     * 操作时间
     */
    private Long operateTime;

    /**
     * 操作模块
     */
    private String module;

    /**
     * 操作类型
     */
    private String operateType;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 成功标志
     */
    private String success;

    /**
     * 操作描述
     */
    private String description;

}