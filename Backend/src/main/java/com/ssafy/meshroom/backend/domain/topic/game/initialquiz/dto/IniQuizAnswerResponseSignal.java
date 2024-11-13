package com.ssafy.meshroom.backend.domain.topic.game.initialquiz.dto;

import lombok.*;

@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IniQuizAnswerResponseSignal {
    private String ovToken;
    private String userName;
    private String submittedWord;
    private boolean result;
}
