package com.zjy.sdk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zjy.common.model.ChatRequest;
import com.zjy.common.model.ChatResponse;
import com.zjy.sdk.callback.ChatResponseCallback;
import com.zjy.common.model.ClientSession;
import com.zjy.common.model.RequestConstant;
import com.zjy.sdk.handler.SessionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

/**
 *
 * 1. 客户端 在启动时通过调用SDK {@code client.connect()}  建立与 WebSocket 连接 <br/>
 *
 * 2. 订阅 client.login() 订阅topic /chat/response/{uniqueId}，
 *  并传递userId、params等参数，SDK中维护一个Map结构：uniqueId -> ClientSession <br/>
 *
 * 3. 通过SessionSubscribeEvent获取uniqueId、clientId、userId、params等，
 *  构建UserSession，并用map结构存储uniqueId和UserSession的对应关系
 *  调用 client.chat() 将用户说的话发送给 服务端 <br/>
 *
 * 4.调用 client.logout() 取消订阅topic，TiBot删除UserSession的对应关系，SDK中删除ClientSession <br/>
 *
 * 5. 当断线时，
 *     方案1：TiBot通过SessionDisconnectEvent获取sessionId，并获取对应的订阅列表，然后将这些订阅关联的UserSession删除
 *     方案2：根据UnsubscribeEnvent，删除关联的UserSession删除
 *
 * 6. 当断线重连时，CC从SDK获取所有的ClientSession，重新订阅
 *     {@link com.zjy.sdk.handler.SessionHandler SessionHandler} 内会重新订阅所有连接
 *
 * 7. 心跳检测 <br/>
 * </pre>
 *
 * @author from
 * @date 2019/03/13
 */
@Slf4j
public class MyWebSocketClient {

    /**
     * stomp客户端
     */
    private WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());

    /**
     * SDK 维护 clientSession，不需要 CC 再自己维护 uniqueId -> clientSession
     */
    private Map<String, ClientSession> clientSessionMap = new ConcurrentHashMap<>();

    /**
     * 每个 uniqueId 对应的订阅 uniqueId -> subscription
     */
    private Map<String, StompSession.Subscription> subscriptionMap = new ConcurrentHashMap<>();

    /**
     * 连接会话
     */
    private StompSession session;

    private ClientWebSocketConfiguration configuration;

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * WebSocket
     */
    private String url;

    private ChatResponseCallback callback;

    public MyWebSocketClient(@NonNull ClientWebSocketConfiguration configuration, ChatResponseCallback callback) {
        this.configuration = configuration;
        this.stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        this.url = "ws://" + configuration.getHost() + "/buling";
        this.callback = callback;
        connect();
    }

    /**
     * 建立与 buling Server WebSocket 连接，
     */
    public void connect() {

        log.info("[ws-client] connect to client");


        // 与鉴权相关的信息  [此处不完善!!!!]
        Map<String, String> queryParams = new HashMap<>(3);
        queryParams.put(RequestConstant.ACCESS_KEY_ID, configuration.getAccessKeyId());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        queryParams.put(RequestConstant.TIMESTAMP, sdf.format(new Date()));
        queryParams.put(RequestConstant.AccessKeySecret, configuration.getAccessKeySecret());


        WebSocketHttpHeaders httpHeaders = new WebSocketHttpHeaders();
        httpHeaders.setAll(queryParams);
        httpHeaders.set(RequestConstant.SIGNATURE, "");

        StompHeaders stompHeaders = new StompHeaders();

        SessionHandler sessionHandler = new SessionHandler(this);

        // 定义并设置用于心跳检测的调度器
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.initialize();
        stompClient.setTaskScheduler(taskScheduler);

        try {
            session = stompClient.connect(url, httpHeaders, stompHeaders, sessionHandler).get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("[ws-client] connect error! ", e);
        }
    }

    /**
     * 进入机器人结点时，将该机器人进行订阅
     *
     * @param clientSession ClientSession
     */
    public void login(ClientSession clientSession) {

        log.debug("[ws-client] Initiated a subscription, uniqueId {}", clientSession.getUniqueId());
        StompHeaders headers = new StompHeaders();
        headers.setDestination("/chat/response/" + clientSession.getUniqueId());
        headers.set("userId", String.valueOf(clientSession.getUserId()));
        headers.set("uniqueId", clientSession.getUniqueId());
        headers.set("clientId", clientSession.getClientId());
        try {
            headers.set("params", objectMapper.writeValueAsString(clientSession.getParams()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        if (session == null || !session.isConnected()) {
            connect();
        }
        StompSession.Subscription subscription = session.subscribe(headers,
                new StompFrameHandler() {
                    @Override
                    @NonNull
                    public Type getPayloadType(@NonNull StompHeaders headers) {
                        return ChatResponse.class;
                    }

                    @Override
                    public void handleFrame(@NonNull StompHeaders headers, Object payload) {
                        if (payload instanceof ChatResponse) {
                            ChatResponse chatResponse = (ChatResponse) payload;
                            log.debug("[ws-client] receive a response uniqueId {} ChatResponse {}, timestamp is {}",
                                    chatResponse.getUniqueId(), chatResponse, System.currentTimeMillis());
                            callback.callback(chatResponse);
                        }
                    }
                });

        // 订阅成功时加入 clientSession 及 subscriptionMap
        clientSessionMap.put(clientSession.getUniqueId(), clientSession);
        subscriptionMap.put(clientSession.getUniqueId(), subscription);

    }

    /**
     * 取消订阅
     *
     * @param uniqueId  uniqueId
     */
    public void logout(String uniqueId) {
        StompSession.Subscription subscription = subscriptionMap.get(uniqueId);
        if (subscription != null) {
            subscription.unsubscribe();
            clientSessionMap.remove(uniqueId);
            subscriptionMap.remove(uniqueId);
        }
    }

    public void chat(ChatRequest chatRequestModel) {
        log.debug("[/chat] uniqueId {} ChatRequest {}, timestamp is {}",
                chatRequestModel.getUniqueId(), chatRequestModel, System.currentTimeMillis());
        session.send("/app/chat", chatRequestModel);
    }

    public void welcome(ChatRequest chatRequestModel) {
        log.debug("[/welcome]  uniqueId {} ChatRequest {}, timestamp is {}", chatRequestModel.getUniqueId(),
                chatRequestModel, System.currentTimeMillis());
        session.send("/app/welcome", chatRequestModel);
    }

    /**
     * 关闭与server的WebSocket连接
     */
    public void disconnect() {
        log.info("disconnect websocket client");
        session.disconnect();
    }

    public boolean isConnected() {
        if (session == null || !session.isConnected()) {
            return false;
        }
        return true;
    }

    public int activeSessionCount() {
        return clientSessionMap.size();
    }
}
