package com.tenco.web_socket_step.stomp;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class StompController {
    private final StompChatService stompChatService;

    // http://localhost:8080/stomp/chat/1
    @GetMapping("/stomp/chat/{roomId}")
    public String index(@PathVariable(name = "roomId") Long roomId, Model model) {

        model.addAttribute("roomId", roomId);// 프론트엔드에서 방번호를 쓸 수 있도록 전달
        model.addAttribute("chatList", stompChatService.findMessageByRoomId(roomId));
        return "stomp/index";
    }

    // 메세지 수신 및 라우팅
    // 클라이언트 "/pub/chat/message/{roomId} 경로로 메세지를 던지면 이 메소드가 실행됨
    // RequestMapping이랑 대응
    // @DestinationVariable: STOMP 목적지 경로에 포함된 동적변수 (roomId)를 가로챔
    // @PathVariable과 대응
    // 메세지 수신(publish처리)
    @MessageMapping("/chat/message/{roomId}")
    public void receiveMessage(@DestinationVariable("roomId") Long roomId, Map<String, String> payload) {
        // DTO대신 Map을 사용해서 JSON 데이터를 받을 수 있다.(Jackson이 자동변환해줌)
        String sender = payload.get("sender");
        String message = payload.get("message");
        stompChatService.saveAndBroadCast(roomId, message, sender);
    }

    // SEND
    // destination: /pub/chat/message/5를 낚아챔
    // content-length: 41
    // content-type: application/json
    // {"sender": "홍길동", "message" : "안녕하세요"}
}
