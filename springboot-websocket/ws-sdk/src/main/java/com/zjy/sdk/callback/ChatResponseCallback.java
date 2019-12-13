package com.zjy.sdk.callback;


import com.zjy.common.model.ChatResponse;

/**
 * ChatResponse 回调接口定义
 * @author zhaojy
 * @date 2019/12
 */
public interface ChatResponseCallback {

    /**
     * 回调方法
     *
     * @param event
     */
    void callback(ChatResponse event);
}
