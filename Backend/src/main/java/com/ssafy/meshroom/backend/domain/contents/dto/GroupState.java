package com.ssafy.meshroom.backend.domain.contents.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public class GroupState {
    String sessionId;
    Boolean isFinish;
}
