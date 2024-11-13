package com.ssafy.meshroom.backend.global.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "openvidu")
public class OpenViduProperties {
    private String url;
    private String secret;
}
