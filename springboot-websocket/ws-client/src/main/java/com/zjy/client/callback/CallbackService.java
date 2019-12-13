package com.zjy.client.callback;

import com.zjy.common.model.ChatResponse;
import com.zjy.sdk.callback.ChatResponseCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 实现SDK的响应接收服务
 *
 * @author zahojy
 * @date 2019/12
 */
@Service
public class CallbackService implements ChatResponseCallback {

    private Logger logger = LoggerFactory.getLogger(CallbackService.class);

    @Override
    public void callback(ChatResponse chatResponse) {
        logger.info("[客户端接收到]: " + chatResponse);
    }
}
