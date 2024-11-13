package com.ssafy.meshroom.backend.domain.session.dto;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SessionInfoResponse {
    private Long maxUserCount;
    private Long currentUserCount;
    private String url;
    private List<SubSessionInfoResponse> groups;
}
