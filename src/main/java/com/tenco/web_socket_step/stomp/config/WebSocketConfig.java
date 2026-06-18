package com.tenco.web_socket_step.stomp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration // IoC
@EnableWebSocketMessageBroker // WebSocket 메세지 브로커를 활성화해서 STOMP 프로토콜 기반의 통신을 설정한다.
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 클라이언트는 웹소켓 연결을 하기 위해서 경로 ws://localhost:8080/ws-stomp 연결을 시도함
        registry.addEndpoint("/ws-stomp")
                .setAllowedOriginPatterns("*");
        // CORS(교차출처허용) : 현재는 모든 도메인에서 요청 및 응답 허용
        // 실무 운영 환경에서는 특정 도메인을 등록해서 사용함
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 1. 우체통 설정 - 발행경로 설정
        // 클라이언트가 서버에게 편지를 보낼때(송신) 붙이는 시작주소(접두사)이다.
        // 예) 클라이언트가 /pub/chat/message를 던지면 @MessageMapping() 컨트롤러가 받아서 처리함.
        registry.setApplicationDestinationPrefixes("/pub");

        /**
         * 2. 개인 우편함 설정 - 구독 설정
         * 클라이언트가 서버로부터 편지를 받기위해(수신) 대기하는 시작주소(접두사)이다.
         * enableSimpleBroker : 외부 브로커(카프카, 래빗MQ 등) 연동없이
         * 스프링부트 메모리안에 가볍고 빠른내장 우체국 분류센터를 만든다.
         * 클라이언트가 /sub/chat/{roomId}를 구독해두면 서버가 해당주소를 찾아서 메세지보내줌
         */
        registry.enableSimpleBroker("/sub");
    }
}
