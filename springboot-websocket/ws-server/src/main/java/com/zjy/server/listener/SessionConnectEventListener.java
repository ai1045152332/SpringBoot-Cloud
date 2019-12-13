package com.zjy.server.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;


/**
 * Websocket 建立连接后，当客户端发送 STOMP connect 命令时触发该事件
 * @author 侯法超
 * @date 2019/03/13
 */
@Component
public class SessionConnectEventListener implements ApplicationListener<SessionConnectEvent> {

    private Logger logger = LoggerFactory.getLogger(SessionConnectEventListener.class);



    @Override
    public void onApplicationEvent(@NonNull SessionConnectEvent event) {

        logger.debug("[WebSocket] 监听到一个ws客户端开启了连接"+event.getMessage().getHeaders());

    }


}
