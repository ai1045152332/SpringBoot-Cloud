package com.zjy.common.model;

import lombok.Data;

import java.util.Map;

/**
 * 注册时使用的信息
 */
@Data
public class ClientSession {

    /**
     * cdr 中的 uniqueId
     */
    private String uniqueId;

    /**
     * 用户id 企业id
     */
    private Integer userId;

    /**
     * 客户号码
     */
    private String clientId;

    /**
     * 请求参数
     */
    private Map<String, String> params;

}
