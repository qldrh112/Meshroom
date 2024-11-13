package com.ssafy.meshroom.backend.domain.topic.game.trueorfalse.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Document("content_TrueFalse")
public class TFInfo {
    @Id
    @Indexed(unique=true)
    private String _id;

    private String userName;
    private String ovToken;
    private String sessionId;
//    private ArrayList<String> truths;
//    private String false1;
    private ArrayList<String> statements;
    private int falseIndex;
}
