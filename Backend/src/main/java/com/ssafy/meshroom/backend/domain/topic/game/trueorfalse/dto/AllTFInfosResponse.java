package com.ssafy.meshroom.backend.domain.topic.game.trueorfalse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AllTFInfosResponse {
    List<TFInfoResponse> allTFInfos;

}
