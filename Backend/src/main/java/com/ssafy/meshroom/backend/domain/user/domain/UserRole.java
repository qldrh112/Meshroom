package com.ssafy.meshroom.backend.domain.user.domain;
//import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    FACILITATOR("진행자"),
    PARTICIPANT("참여자"),
    TEAM_LEADER("팀장");

    private final String koreanName;

    UserRole(String koreanName) {
        this.koreanName = koreanName;
    }

    public String getKoreanName() {
        return this.koreanName;
    }

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }

    // 기존의 메서드들...
}