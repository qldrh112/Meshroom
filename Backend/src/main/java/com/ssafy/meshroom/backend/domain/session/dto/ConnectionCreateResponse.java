package com.ssafy.meshroom.backend.domain.session.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ConnectionCreateResponse {
    String ovToken;
    String userId;
}
