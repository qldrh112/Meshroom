package com.ssafy.meshroom.backend.domain.topic.game.initialquiz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IniQuizAdministratorSignal {
    private String sessionId;
    private int curStep;
    private int submitCount;
    private int finishCount;
}
