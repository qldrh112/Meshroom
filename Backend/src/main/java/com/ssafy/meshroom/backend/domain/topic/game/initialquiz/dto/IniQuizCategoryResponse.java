package com.ssafy.meshroom.backend.domain.topic.game.initialquiz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class IniQuizCategoryResponse {
    private String categoryName;
}
