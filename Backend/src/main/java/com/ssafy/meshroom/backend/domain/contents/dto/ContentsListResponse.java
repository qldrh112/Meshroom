package com.ssafy.meshroom.backend.domain.contents.dto;

import com.ssafy.meshroom.backend.domain.contents.domain.Contents;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class ContentsListResponse {
    private List<Contents> contents;
}
