package com.ssafy.meshroom.backend.domain.topic.chat.service;

import com.ssafy.meshroom.backend.domain.topic.chat.dto.ChatMessageSubscribe;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class KafkaChatProducer {
    private static final String TOPIC_CHAT_MESSAGE = "chat-message";

    private final KafkaTemplate<String, ChatMessageSubscribe> kafkaTemplate;

    public void sendMessage(ChatMessageSubscribe chatMessageSubscribe) {

        log.info(">>>>>>>> kafka send message : " +  chatMessageSubscribe);
        kafkaTemplate.send(TOPIC_CHAT_MESSAGE, chatMessageSubscribe);
    }
}
