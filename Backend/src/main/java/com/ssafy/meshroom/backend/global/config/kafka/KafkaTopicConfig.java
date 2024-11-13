package com.ssafy.meshroom.backend.global.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic chatTopic() {
        return TopicBuilder.name("chat-message")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic meshTopic() {
        return TopicBuilder.name("game-touchmesh")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
