package com.ssafy.meshroom.backend.global.error.exception;

import com.ssafy.meshroom.backend.global.error.code.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Custom exception for REST API errors.
 */
@Getter
@RequiredArgsConstructor
public class RestApiException extends RuntimeException {

    private final ErrorCode errorCode; // Error code associated with the exception
}
