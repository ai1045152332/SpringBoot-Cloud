package com.example.demo.core;

/**
 * 操作日志注解
 *
 * @author zhaojy
 * @date 2019/10/17
 */
public enum OperationLogSuccess {

    /**
     * 操作成功
     */
    SUCCESS(0,"成功"),

    /**
     * 操作失败，失败情况包括：程序抛出异常，http返回码非 200
     */
    FAILURE(1,"失败");

    private Integer code;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    OperationLogSuccess(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }
}
