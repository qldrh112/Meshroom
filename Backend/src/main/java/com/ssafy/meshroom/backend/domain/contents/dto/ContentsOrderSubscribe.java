package com.ssafy.meshroom.backend.domain.contents.dto;

import lombok.*;

import java.util.List;

@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContentsOrderSubscribe {
    private String contentsId;
    private Long contentsSequence;
    private Long totalContentsCount;
    private Long finishGroupCount;
    private Long totalGroupCount;
    private List<GroupState> currentGroupState;
}
