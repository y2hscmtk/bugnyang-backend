package com.winner_cat.global.exception;

import com.winner_cat.global.enums.statuscode.BaseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

// For Custom Exception
@RequiredArgsConstructor
public class GeneralException extends RuntimeException{
    private final BaseCode errorStatus;

    public String getErrorCode() {
        return errorStatus.getCode();
    }

    public String getErrorReason() {
        return errorStatus.getMessage();
    }

    public HttpStatus getHttpStatus() {
        return errorStatus.getHttpStatus();
    }

    @Override
    public String getMessage() {
        return errorStatus.getMessage();
    }
}
