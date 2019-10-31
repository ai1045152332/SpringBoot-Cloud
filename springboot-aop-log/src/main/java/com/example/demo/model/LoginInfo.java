package com.example.demo.model;

import lombok.Data;

/**
 * 操作日志注解
 *
 * @author zhaojy
 * @date 2019/10/17
 */
@Data
public class LoginInfo {
    /**
     * 账户登录名
     */
    private String accountLoginName;

    /**
     * 企业ID (部门ID)
     */
    private String enterpriseId;

    /**
     * 操作人
     */
    private String operator;
}
