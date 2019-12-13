package com.zjy.client.controller;


import com.zjy.common.model.ChatRequest;
import com.zjy.common.model.ClientSession;
import com.zjy.sdk.MyWebSocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * HTTP 请求测试
 */
@RestController
public class TestController {

    private final MyWebSocketClient WSClient;


    @Autowired
    public TestController(MyWebSocketClient WSClient) {
        this.WSClient = WSClient;
    }

    @RequestMapping("/connect")
    public Object connect() {
        WSClient.connect();
        return "connected";
    }

    @RequestMapping("/login")
    public Object login(String uniqueId, Integer userId, String clientId) {

        ClientSession clientSession = new ClientSession();
        clientSession.setUniqueId(uniqueId);
        clientSession.setUserId(userId);
        clientSession.setClientId(clientId);

        Map<String, String> params = new HashMap<>(16);
        params.put("key1", "value1");
        params.put("key2", "value2");
        clientSession.setParams(params);

        WSClient.login(clientSession);
        return "login";
    }

    @RequestMapping("/chat")
    public Object chat(String uniqueId, String query,String type) {
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setPlayStatus(false);
        chatRequest.setQuery(query);
        chatRequest.setType(type);
        chatRequest.setUniqueId(uniqueId);
        WSClient.chat(chatRequest);
        return "chat";
    }

    @RequestMapping("/welcome")
    public Object welcome(String uniqueId, String query,String type) {
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setPlayStatus(false);
        chatRequest.setQuery(query);
        chatRequest.setType(type);
        chatRequest.setUniqueId(uniqueId);
        WSClient.welcome(chatRequest);
        return "chat";
    }

    @RequestMapping("/logout")
    public Object logout(String uniqueId) {
        WSClient.logout(uniqueId);
        return "logout";
    }


    @RequestMapping("/disconnect")
    public Object disconnect() {
        WSClient.disconnect();
        return "disconnected";
    }

    @RequestMapping("/activeSessionCount")
    public Object activeSessionCount() {
        return WSClient.activeSessionCount();
    }
}
