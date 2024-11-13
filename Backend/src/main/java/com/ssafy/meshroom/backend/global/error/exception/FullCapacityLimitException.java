package com.ssafy.meshroom.backend.global.error.exception;

import com.ssafy.meshroom.backend.global.error.code.CommonErrorCode;
import com.ssafy.meshroom.backend.global.error.code.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Custom exception for REST API errors.
 */
@Getter
@RequiredArgsConstructor
public class FullCapacityLimitException extends RuntimeException {

    private final ErrorCode errorCode = CommonErrorCode.FULL_CAPACITY_USERS; // Error code associated with the exception
}
