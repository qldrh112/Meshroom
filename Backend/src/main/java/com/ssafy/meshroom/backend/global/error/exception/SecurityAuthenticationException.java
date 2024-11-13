package com.ssafy.meshroom.backend.global.error.exception;

import com.ssafy.meshroom.backend.global.error.code.CommonErrorCode;
import com.ssafy.meshroom.backend.global.error.code.ErrorCode;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class SecurityAuthenticationException extends AuthenticationException {
    private final ErrorCode errorCode = CommonErrorCode.UNAUTHORIZED;
    public SecurityAuthenticationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public SecurityAuthenticationException(String msg) {
        super(msg);
    }
}
