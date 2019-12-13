package com.zjy.server.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;


/**
 * WebSocket 断开连接处理，这个时候需要清除与此 session 相关的所有 UserSession 与 redis botSession。
 * 异常断开时不会有 SessionSubscribeEvent，需要在这里清理<br/>
 * 当连接
 *
 * @author 侯法超
 * @date 2019/03/13
 */
@Component
public class SessionDisconnectEventListener implements ApplicationListener<SessionDisconnectEvent> {

    private Logger logger = LoggerFactory.getLogger(SessionDisconnectEventListener.class);


    @Override
    public void onApplicationEvent(@NonNull SessionDisconnectEvent event) {

        String sessionId = event.getSessionId();

//        logger.info("[WS断开连接]事件 sessionId:{}, code:{}, reason:{}", sessionId, event.getCloseStatus().getCode(), event.getCloseStatus().getReason());

    }
}
