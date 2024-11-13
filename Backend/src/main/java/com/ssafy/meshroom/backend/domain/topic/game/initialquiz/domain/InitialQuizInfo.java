package com.ssafy.meshroom.backend.domain.topic.game.initialquiz.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Document("content_InitialQuiz")
public class InitialQuizInfo {
    @Id
    @Indexed(unique=true)
    private String _id;

    private String userName;
    private String ovToken;
    private String sessionId;
    private String categoryName;
    private String word;
}
