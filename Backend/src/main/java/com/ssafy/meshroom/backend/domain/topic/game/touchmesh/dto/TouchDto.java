package com.ssafy.meshroom.backend.domain.topic.game.touchmesh.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TouchDto {
    //  세션id와 +/- 정보
    private String mainSessionId;
    private String sessionId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private TouchType type;

    public enum TouchType {
        INCREASE, DECREASE;
    }
}