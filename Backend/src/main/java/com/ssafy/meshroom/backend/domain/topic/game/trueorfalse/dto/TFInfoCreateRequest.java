package com.ssafy.meshroom.backend.domain.topic.game.trueorfalse.dto;

import lombok.*;

import java.util.ArrayList;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TFInfoCreateRequest {
    private String ovToken;

    private ArrayList<String> truths;
    private String false1;
}
