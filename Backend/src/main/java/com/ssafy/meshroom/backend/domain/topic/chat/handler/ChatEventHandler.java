package com.ssafy.meshroom.backend.domain.topic.chat.handler;

import com.ssafy.meshroom.backend.domain.topic.chat.service.ChatService;
import com.ssafy.meshroom.backend.domain.topic.chat.dto.ChatMessagePublish;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@Slf4j
@AllArgsConstructor
public class ChatEventHandler {

    private final ChatService chatService;

    @MessageMapping("/chat/join")
    public void join(Principal p, ChatMessagePublish chatMessagePublish) {
        log.info("MessageMapping /chat/join");

        String userSid = ((UsernamePasswordAuthenticationToken)p).getName();
        chatService.join(userSid, chatMessagePublish);
    }

    @MessageMapping("/chat/leave")
    public void leave(Principal p, ChatMessagePublish chatMessagePublish) {
        log.info("MessageMapping /chat/leave");

        String userSid = ((UsernamePasswordAuthenticationToken)p).getName();
        chatService.leave(userSid, chatMessagePublish);
    }


    // Client가 SEND할 수 있는 경로
    // WebSocketConfig에서 설정한 applicationDestinationPrefixes와 @MessageMapping 경로가 병합됨
    // "/publish/chat/message"
    @MessageMapping("/chat/message")
//    @SendTo("/chat/message")
    public void sendMessage(Principal p, ChatMessagePublish chatMessagePublish) {
        log.info("Message Mapping /chat/message");

        log.info(((UsernamePasswordAuthenticationToken)p).getAuthorities().toString());
        String userSid = ((UsernamePasswordAuthenticationToken)p).getName();
        log.info("userSid : " + userSid);

        chatService.sendChatMessage(userSid, chatMessagePublish);
    }
}
