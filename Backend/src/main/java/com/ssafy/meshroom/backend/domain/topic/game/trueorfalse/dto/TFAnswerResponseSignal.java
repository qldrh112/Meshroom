package com.ssafy.meshroom.backend.domain.topic.game.trueorfalse.dto;

import lombok.*;

@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TFAnswerResponseSignal {
    private String ovToken;
    private String userName;
    private int chosen;
}
