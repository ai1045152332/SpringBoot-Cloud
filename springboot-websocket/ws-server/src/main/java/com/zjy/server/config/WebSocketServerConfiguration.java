package com.zjy.server.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.*;


/**
 * 定义使用来自WebSocket客户端的简单消息传递协议(例如STOMP)配置消息处理的方法
 * 注释@EnableWebSocketMessageBroker开始使用STOMP协议来传输基于代理（message broker）的消息
 *
 * @author zhaojy
 * @date 2019/12
 */
@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketServerConfiguration implements WebSocketMessageBrokerConfigurer {

    /**
     * 注册STOMP协议的节点（endpoint） 将每个节点映射到一个特定的URL
     *
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/buling");
//                .setAllowedOrigins("*");


        //建立连接端点，注册一个STOMP的协议节点,并指定使用SockJS协议
        registry.addEndpoint("/gs-guide-websocket")
                .setAllowedOrigins("http://localhost:8086")
                .withSockJS();


//        registry.addEndpoint("/stomp")
//                .setHandshakeHandler(new DefaultHandshakeHandler() {
//                    @Override
//                    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
//                        //将客户端标识封装为Principal对象，从而让服务端能通过getName()方法找到指定客户端
//                        Object o = attributes.get("name");
//                        return new FastPrincipal(o.toString());
//                    }
//                })
//                //添加socket拦截器，用于从请求中获取客户端标识参数
//                .addInterceptors(new HandleShakeInterceptors()).withSockJS();
    }


    /**
     * 配置与处理从WebSocket客户端接收和发送的消息相关的选项。
     *
     * @param registration
     */
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setSendTimeLimit(10 * 1000);
        registration.setSendBufferSizeLimit(128 * 1024);
        registration.setMessageSizeLimit(16 * 1024);
    }

    /**
     * 配置消息代理
     *
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        // 定义用于心跳检测的调度器
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.initialize();

        // 客户端发送消息所需要的请求前缀
        registry.setApplicationDestinationPrefixes("/app");

        // 客户端订阅消息的请求前缀buling，topic一般用于广播推送，queue&&chat用于点对点推送
        registry.enableSimpleBroker("/buling", "/topic", "/queue", "/chat")
                // 心跳设置，具体解释见
                // 1) setTaskScheduler 方法的javadoc
                // 2) https://docs.spring.io/spring/docs/5.1.5.RELEASE/spring-framework-reference/web.html#websocket-stomp-handle-simple-broker
                // 3) http://stomp.github.io/stomp-specification-1.2.html#Heart-beating
                .setTaskScheduler(taskScheduler);
        // 服务端通知客户端的前缀，可以不设置，默认为user
        registry.setUserDestinationPrefix("/user");
        registry.setCacheLimit(20000);

        /*  如果是用自己的消息中间件，则按照下面的去配置，删除上面的配置
         *   registry.enableStompBrokerRelay("/topic", "/queue")
            .setRelayHost("rabbit.someotherserver")
            .setRelayPort(62623)
            .setClientLogin("marcopolo")
            .setClientPasscode("letmein01");
            registry.setApplicationDestinationPrefixes("/app", "/foo");
         * */
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.taskExecutor().corePoolSize(100);

    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.taskExecutor().corePoolSize(100);
    }

}
