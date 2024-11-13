package com.ssafy.meshroom.backend.domain.user.api;

import com.ssafy.meshroom.backend.domain.user.application.UserNameService;
import com.ssafy.meshroom.backend.domain.user.dto.UserNameUpdateRequest;
import com.ssafy.meshroom.backend.global.common.dto.Response;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserNameService userNameService;

    @PatchMapping("/username")
    public ResponseEntity<Response<?>> patchUserName(
            @RequestBody UserNameUpdateRequest userNameUpdateRequest
            ) throws OpenViduJavaClientException, OpenViduHttpException {
        return ResponseEntity.status(HttpStatus.OK).body(userNameService.changeUserName(userNameUpdateRequest.getUserName()));
    }

}
