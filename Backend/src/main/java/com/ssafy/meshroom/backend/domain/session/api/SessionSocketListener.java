package com.ssafy.meshroom.backend.domain.session.api;

import com.ssafy.meshroom.backend.domain.OVToken.application.OVTokenService;
import com.ssafy.meshroom.backend.domain.session.application.SessionService;
import com.ssafy.meshroom.backend.domain.session.domain.Session;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import java.security.Principal;

@Service
@Slf4j
@RequiredArgsConstructor
public class SessionSocketListener {

    private final SessionService sessionService;
    private final OVTokenService ovTokenService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @EventListener
    public void sessionConnectedEventHandler(SessionConnectedEvent event) throws OpenViduJavaClientException, OpenViduHttpException {
        // session service의 session info 보내기
        Principal p = event.getUser();
        log.info(p.getName());
        Session session = ovTokenService.getMainSessionFromUserId(p.getName());
        // session service의 session info 보내기
        sendMessage(session);

        log.info("SessionConnectedEvent = {}", event);
    }

//    // 새로고침 흐름 토론 필요
//    @EventListener
//    public void sessionDisconnectEventHandler(SessionDisconnectEvent event) throws OpenViduJavaClientException, OpenViduHttpException {
//        Principal p = event.getUser();
//        // sessionId 에 해당하는 토큰 삭제 ovTokenService 에서 삭제
//
//        Session session = ovTokenService.deleteAllSubSessionConnectionFromUserId(p.getName());
//        log.info("session remove!!!");
//
//        // session service의 session info 보내기
//        sendMessage(session);
//
//        log.info("SessionDisconnectEvent = {}", event);
//    }

    private void sendMessage(Session session) throws OpenViduJavaClientException, OpenViduHttpException {
        boolean isMain = session.getIsMain();
        if(isMain){
            String sessionId = session.getSessionId();
            simpMessagingTemplate.convertAndSend("/subscribe/sessions/" + sessionId, sessionService.getSessionInfo(sessionId).getResult());
        }else{
            String mainSessionId = session.getMainSession();
            simpMessagingTemplate.convertAndSend("/subscribe/sessions/" + mainSessionId, sessionService.getSessionInfo(mainSessionId).getResult());
        }
    }

}
