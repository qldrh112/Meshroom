package com.ssafy.meshroom.backend.domain.topic.chat.service;

import com.ssafy.meshroom.backend.domain.topic.chat.dto.ChatMessagePublish;
import com.ssafy.meshroom.backend.domain.topic.chat.dto.ChatMessageSubscribe;
import com.ssafy.meshroom.backend.domain.session.application.SessionService;
import com.ssafy.meshroom.backend.domain.user.dao.UserRepository;
import com.ssafy.meshroom.backend.domain.user.domain.User;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ChatService {

    private final User user;
    private final SimpMessagingTemplate template;
    private final SessionService sessionService;
    private final KafkaChatProducer kafkaChatProducer;
    private final UserRepository userRepository;

    public void sendChatMessage(String userSid, ChatMessagePublish chatMessagePublish) {
        ChatMessageSubscribe chatMessageSubscribe = processChatMessage(userSid, chatMessagePublish);

        kafkaChatProducer.sendMessage(chatMessageSubscribe);
    }

    public void join(String userSid, ChatMessagePublish chatMessagePublish) {
        ChatMessageSubscribe chatMessageSubscribe = processChatMessage(userSid, chatMessagePublish);

        chatMessageSubscribe.setContent(chatMessageSubscribe.getUserName() + " 님이 입장하셨습니다.");

        kafkaChatProducer.sendMessage(chatMessageSubscribe);
    }

    // TODO: 퇴장 처리 로직 추가해야 합니다.
    public void leave(String userSid, ChatMessagePublish chatMessagePublish) {
        ChatMessageSubscribe chatMessageSubscribe = processChatMessage(userSid, chatMessagePublish);

        chatMessageSubscribe.setContent(chatMessageSubscribe.getUserName() + " 님이 퇴장하셨습니다.");

        kafkaChatProducer.sendMessage(chatMessageSubscribe);
        try {
            sessionService.removeUserFromSession(chatMessageSubscribe.getSessionId(), user.get_id());
        } catch (OpenViduJavaClientException e) {
            throw new RuntimeException(e);
        } catch (OpenViduHttpException e) {
            throw new RuntimeException(e);
        }
    }

    public ChatMessageSubscribe processChatMessage(String userSid, ChatMessagePublish chatMessagePublish) {
        String userName = userRepository.findById(userSid)
                .orElseThrow(() -> new RuntimeException("유저 없음"))
                .getUsername();

        ChatMessageSubscribe chatMessageSubscribe = ChatMessageSubscribe.from(chatMessagePublish, userName);
        System.out.println(chatMessageSubscribe);
        return chatMessageSubscribe;
    }
}
