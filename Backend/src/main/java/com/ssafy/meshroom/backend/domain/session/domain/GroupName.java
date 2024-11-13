package com.ssafy.meshroom.backend.domain.session.domain;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Document(collection="groupNames")
public class GroupName {
    String groupName;
}
