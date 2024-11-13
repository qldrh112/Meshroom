package com.ssafy.meshroom.backend.domain.user.application;

import com.ssafy.meshroom.backend.domain.OVToken.application.OVTokenService;
import com.ssafy.meshroom.backend.domain.session.application.SessionService;
import com.ssafy.meshroom.backend.domain.session.domain.Session;
import com.ssafy.meshroom.backend.domain.user.dao.UserRepository;
import com.ssafy.meshroom.backend.domain.user.domain.User;
import com.ssafy.meshroom.backend.global.common.dto.Response;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserNameService {
    private final UserRepository userRepository;
    private final SessionService sessionService;
    private final OVTokenService ovTokenService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public Response<?> changeUserName(String username) throws OpenViduJavaClientException, OpenViduHttpException {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User foundUser = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("유저 없음"));

        foundUser.setUsername(username);
        User user = userRepository.save(foundUser);
        Session mainSession = ovTokenService.getMainSessionFromUserId(user.get_id());
        String mainSessionId = mainSession.getSessionId();
        simpMessagingTemplate.convertAndSend("/subscribe/sessions/"+mainSessionId, sessionService.getSessionInfo(mainSessionId).getResult());

        return new Response<>(true, 2000L, "유저 닉네임 정보가 성공적으로 수정되었습니다.", null);
    }
}
