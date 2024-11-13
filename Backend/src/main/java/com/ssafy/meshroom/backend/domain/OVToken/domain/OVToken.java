package com.ssafy.meshroom.backend.domain.OVToken.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Document(collection = "ovTokens")
public class OVToken {
    @Id
    @Indexed(unique = true)
    private String _id;

    private String userSid;

    private String sessionSid;

    private String ovToken;

    private Long currCount;
}
