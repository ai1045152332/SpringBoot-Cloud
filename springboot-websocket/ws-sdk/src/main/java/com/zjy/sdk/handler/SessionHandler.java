package com.zjy.sdk.handler;


import com.zjy.sdk.MyWebSocketClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.*;

import javax.websocket.DeploymentException;
import java.net.ConnectException;
import java.util.concurrent.TimeUnit;

/**
 * 客户端 会话生命周期事件的契约
 * <p>
 *
 * @author zhaojy
 * @date 2019/12
 */
@Slf4j
public class SessionHandler extends StompSessionHandlerAdapter {


    private MyWebSocketClient webSocketClient;

    public SessionHandler(MyWebSocketClient wsClient) {
        this.webSocketClient = wsClient;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        System.out.println("handleFrame: " + payload);
    }

    /**
     * 建立WebSocket建立连接后 的回调
     *
     * @param session          the client STOMP session
     * @param connectedHeaders the STOMP CONNECTED frame headers
     */
    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        log.info("[WebSocket] connect client successfully ~(￣▽￣)~ sessionId:{}" + session.getSessionId());
    }

    /**
     * 处理在处理STOMP帧时出现的任何异常
     * <p>
     * 例如: 如未能转换有效负载或应用程序中未处理的异常
     *
     * @param session   the client STOMP session
     * @param command   框架的 STOMP 命令
     * @param headers   headers
     * @param payload   the raw payload 原始的有效载荷
     * @param exception 异常
     */
    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        log.error("handleException command:{} ", command.getMessageType(), exception);
    }

    /**
     * 处理低级传输错误，可能是I/O错误或无法对STOMP消息进行编码或解码。
     *
     * @param session   the client STOMP session
     * @param exception the exception that occurred (发生的异常)
     */
    @Override
    public void handleTransportError(StompSession session, Throwable exception) {

        if (exception instanceof ConnectionLostException) {
            log.error("[handleTransportError] lost connection for client, sleep 8s and retry connect", exception);
            connectRetry(8000);
        } else if (exception instanceof ConnectException) {
            log.error("[handleTransportError] can't connect client, sleep 10s and retry connect", exception);
            connectRetry(10000);
        } else if (exception instanceof DeploymentException) {
            log.error("[DeploymentException] 远程计算机拒绝网络连接。 sleep 10s and retry connect");
            connectRetry(10000);
        }else {
            log.error("[Exception] 未知异常请联系管理员处理", exception);
        }
    }

    /**
     * 断线重连机制
     *
     * @param sleepMillis sleep 毫秒数
     */
    private void connectRetry(long sleepMillis) {
        try {
            TimeUnit.MILLISECONDS.sleep(sleepMillis);
            webSocketClient.connect();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
