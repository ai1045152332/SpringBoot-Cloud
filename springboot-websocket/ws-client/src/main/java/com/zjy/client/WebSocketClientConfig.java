package com.zjy.client;

import com.zjy.sdk.ClientWebSocketConfiguration;
import com.zjy.sdk.MyWebSocketClient;
import com.zjy.sdk.callback.ChatResponseCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 生成ws客户端请求Bean
 *
 * @author zhaojy
 */
@Configuration
public class WebSocketClientConfig {

    private final ChatResponseCallback callback;

    @Autowired
    public WebSocketClientConfig(ChatResponseCallback callback) {
        this.callback = callback;
    }

    @Bean
    public MyWebSocketClient getWebSocketClient() {

        ClientWebSocketConfiguration configuration = new ClientWebSocketConfiguration("localhost:8080", "key", "secret");

        return new MyWebSocketClient(configuration, callback);
    }
}
