package com.ssafy.meshroom.backend.domain.topic.game.touchmesh.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class UpdatedMushroom {
    private String mainSessionId;
    private String sessionId;
    private int size;
}
