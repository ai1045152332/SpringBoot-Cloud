package com.zjy.server.controller;

import com.zjy.common.entity.Greeting;
import com.zjy.common.entity.HelloMessage;
import com.zjy.common.model.ChatRequest;
import com.zjy.common.model.ChatResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.util.HtmlUtils;

@Slf4j
@CrossOrigin
@Controller
public class BotController {

    /**
     * 通过SimpMessagingTemplate 向浏览器发生消息
     */
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * /app/chat
     *
     * @param request
     * @param headerAccessor
     */
    @MessageMapping("/chat")
    public void chat(ChatRequest request, SimpMessageHeaderAccessor headerAccessor) {
        log.debug("[服务端接收到/chat]    request:{} headerAccessor:{}", request, headerAccessor);
        sendMessage("1");
        sendMessageToUser(request);
    }


    /**
     *  当浏览器向服务端发生请求时，通过@MessageMapping映射/welcome这个地址
     *  注解@MessageMapping使用方法与@RequestMapping相似
     * @param chatRequest
     * @return
     * @throws Exception
     */
    @MessageMapping("welcome")
    public ChatResponse say(ChatRequest chatRequest) throws Exception {
        log.debug("服务端收到消息:{}",chatRequest);
        ChatResponse chatResponse = new ChatResponse();
        chatResponse.setErrorCode(1);
        return chatResponse;
    }



    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception{

        Thread.sleep(100);
        System.out.println(message);
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }


    /**
     * 点对点发送
     * @SendTo("/chat/response/1")
     */
    public void sendMessageToUser(ChatRequest chatRequest){

        messagingTemplate.convertAndSendToUser("A","/queue/chat",chatRequest);
    }

    /**
     * 发送消息-同步
     *
     * @param uniqueId          唯一标识
     */
    private synchronized void sendMessage(String uniqueId) {
        ChatResponse chatResponse = new ChatResponse();
        chatResponse.setErrorCode(1);
        messagingTemplate.convertAndSend("/chat/response/" + uniqueId, chatResponse);
    }
}
