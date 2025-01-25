package com.boldfaced7.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ErrorResponse {

    private String message;
    private String code;

    private ErrorResponse(final ErrorCode errorCode) {
        message = errorCode.getMessage();
        code = errorCode.getCode();
    }

    private ErrorResponse(final ErrorCode errorCode, String message) {
        this.message = message;
        code = errorCode.getCode();
    }

    public ErrorResponse(String message, String code) {
        this.message = message;
        this.code = code;
    }

    public static ErrorResponse of(final ErrorCode code) {
        return new ErrorResponse(code);
    }

    public static ErrorResponse of(final ErrorCode code, final String message) {
        return new ErrorResponse(code, message);
    }

    public static ErrorResponse of(String message, String code) {
        return new ErrorResponse(message, code);
    }
}
