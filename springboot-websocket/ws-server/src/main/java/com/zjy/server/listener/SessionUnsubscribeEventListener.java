package com.zjy.server.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;


/**
 * 监听:主动挂断 logout 事件
 * 根据 uniqueId 删除对应 UserSession, 及 botSession，对于异常断开连接不会触发。
 *
 * @author from
 * @date 2019/3/16
 */
@Slf4j
@Component
public class SessionUnsubscribeEventListener implements ApplicationListener<SessionUnsubscribeEvent> {

    @Override
    public void onApplicationEvent(@NonNull SessionUnsubscribeEvent event) {

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String subscriptionId = headerAccessor.getSubscriptionId();

        log.info("[取消订阅事件] subscriptionId:[{}]",  subscriptionId);


    }
}
