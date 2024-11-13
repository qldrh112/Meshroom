package com.ssafy.meshroom.backend.domain.topic.game.trueorfalse.dto;

import com.ssafy.meshroom.backend.domain.topic.game.trueorfalse.domain.TFInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TFInfoResponse {
    private String userName;
    private String ovToken;
    private ArrayList<String> statements;
    private int falseIndex;

    public static TFInfoResponse from (TFInfo tfInfo) {
        TFInfoResponse tfInfoResponse = new TFInfoResponse();
        tfInfoResponse.userName = tfInfo.getUserName();
        tfInfoResponse.ovToken = tfInfo.getOvToken();
        tfInfoResponse.statements = tfInfo.getStatements();
        tfInfoResponse.falseIndex = tfInfo.getFalseIndex();

        return tfInfoResponse;
    }
}
