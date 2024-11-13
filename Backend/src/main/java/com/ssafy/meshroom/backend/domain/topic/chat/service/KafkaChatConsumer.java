package com.ssafy.meshroom.backend.domain.topic.chat.service;

import com.ssafy.meshroom.backend.domain.topic.chat.dto.ChatMessageSubscribe;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class KafkaChatConsumer {
    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "chat-message", groupId = "mesh-chat")
    public void listen(ChatMessageSubscribe chatMessage) {

        // 클라이언트로 메시지 전달
        messagingTemplate.convertAndSend("/subscribe/chat/session/" + chatMessage.getSessionId(), chatMessage);
    }
}
