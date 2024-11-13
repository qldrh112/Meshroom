package com.ssafy.meshroom.backend.domain.topic.game.initialquiz.dto;

import lombok.*;

@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IniQuizAnswerRequestSignal {
    private String ownerOvToken;
    private String ovToken;
    private String userName;
    private String guessWord;
}
