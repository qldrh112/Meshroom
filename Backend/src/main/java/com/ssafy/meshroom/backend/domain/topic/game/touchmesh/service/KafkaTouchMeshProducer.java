package com.ssafy.meshroom.backend.domain.topic.game.touchmesh.service;

import com.ssafy.meshroom.backend.domain.OVToken.dao.OVTokenRepository;
import com.ssafy.meshroom.backend.domain.session.dao.SessionRepository;
import com.ssafy.meshroom.backend.domain.topic.game.touchmesh.dto.TouchDto;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class KafkaTouchMeshProducer {

    private KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "game-touchmesh";

    public void sendEvent(TouchDto touchEvent) {

        kafkaTemplate.send(TOPIC, touchEvent);
    }
}
