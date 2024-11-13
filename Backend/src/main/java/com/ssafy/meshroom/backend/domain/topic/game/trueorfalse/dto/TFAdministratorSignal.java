package com.ssafy.meshroom.backend.domain.topic.game.trueorfalse.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TFAdministratorSignal {
    private String sessionId;
    private int curStep;
    private int submitCount;
    private int finishCount;
}
