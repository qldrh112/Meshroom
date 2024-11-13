package com.ssafy.meshroom.backend.domain.session.application;

import com.ssafy.meshroom.backend.global.common.properties.OpenViduProperties;
import io.openvidu.java.client.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OpenViduService {
    private final OpenViduProperties openViduProperties;
    private OpenVidu openvidu;

    @PostConstruct
    public void init() {
        this.openvidu = new OpenVidu(openViduProperties.getUrl(), openViduProperties.getSecret());
    }

    public Session createSession() throws OpenViduJavaClientException, OpenViduHttpException {
        return openvidu.createSession();
    }

    public Session getSession(String sessionId) throws OpenViduJavaClientException, OpenViduHttpException {
        return openvidu.getActiveSession(sessionId);
    }

    public long getSessionCount(Session session) throws OpenViduJavaClientException, OpenViduHttpException {
        session.fetch();
        return session.getActiveConnections().size();
    }

    public List<String> getUsernameInSession(Session session) throws OpenViduJavaClientException, OpenViduHttpException {
        session.fetch();
        return session.getActiveConnections().stream()
                .map(Connection::getServerData)
                .collect(Collectors.toList());
    }

    public void forceDisconnet(Session session, Connection connection) throws OpenViduJavaClientException, OpenViduHttpException {
        session.forceDisconnect(connection);
    }
}
