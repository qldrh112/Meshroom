package com.ssafy.meshroom.backend.domain.session.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Document(collection="sessions")
public class Session {
    @Id
    @Indexed(unique=true)
    private String _id;

    @Indexed(unique=true)
    private String sessionId;

    private Long maxUserCount;

    private Long maxSubuserCount;

    private String url;

    private String groupName;

    private Boolean isMain;

    private String mainSession;

    private Boolean isReady;
}
