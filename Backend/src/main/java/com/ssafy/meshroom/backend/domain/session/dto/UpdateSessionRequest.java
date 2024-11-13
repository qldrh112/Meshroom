package com.ssafy.meshroom.backend.domain.session.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UpdateSessionRequest {
    private Long maxUserCount;
    private Long maxSubuserCount;
}
