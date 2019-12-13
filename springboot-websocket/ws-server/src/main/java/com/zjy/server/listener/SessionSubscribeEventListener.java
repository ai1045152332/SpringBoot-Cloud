package com.zjy.server.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.List;


/**
 * @author from
 * @date 2019/03/14
 */
@Slf4j
@Component
public class SessionSubscribeEventListener implements ApplicationListener<SessionSubscribeEvent> {

    @Override
    public void onApplicationEvent(@NonNull SessionSubscribeEvent event) {
        // 当 client 订阅时，将 构建UserSession加入 map
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        log.info("[订阅事件]");
        String userId = getNativeHeader(headerAccessor, "userId");
        String uniqueId = getNativeHeader(headerAccessor, "uniqueId");
        log.info("[SessionSubscribeEvent] headerAccessor:{}", headerAccessor.toString());
        log.info("[SessionSubscribeEvent] userId:{}  uniqueId:{}", userId, uniqueId);

        // 进行权限验证

        // 保存header信息 到redis

        // 启动定时器等
    }

    @Nullable
    private String getNativeHeader(StompHeaderAccessor headerAccessor, String headerName) {
        List<String> headerValues = headerAccessor.getNativeHeader(headerName);
        if (headerValues != null && headerValues.size() > 0) {
            return headerValues.get(0);
        }
        return null;
    }

}
