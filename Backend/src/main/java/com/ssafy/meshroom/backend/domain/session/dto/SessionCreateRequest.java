package com.ssafy.meshroom.backend.domain.session.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SessionCreateRequest {
    List<String> contentsOrder;
}
