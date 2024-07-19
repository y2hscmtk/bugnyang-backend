package com.winner_cat.global.exception;

import com.winner_cat.global.enums.statuscode.ErrorStatus;
import com.winner_cat.global.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    // 요청 파라미터 유효성 검사 실패 시 호출
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ApiResponse<Object> response = ApiResponse.onFailure(ErrorStatus._BAD_REQUEST.getCode(), "유효성 검사 실패", errors);
        log.error("Validation error: {}", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Custom Error Handler
    // GeneralException 및 하위 클래스에 대한 예외 처리
    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<ApiResponse<?>> handleGeneralException(GeneralException exception) {
        ApiResponse<Object> response = ApiResponse.onFailure(exception.getErrorCode(), exception.getErrorReason(), null);
        log.error("General exception: {}", exception.getErrorReason());
        return new ResponseEntity<>(response, exception.getHttpStatus());
    }

    // 그 외의 모든 예외의 경우
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception exception) {
        ApiResponse<Object> response = ApiResponse.onFailure(
                ErrorStatus._INTERNAL_SERVER_ERROR.getCode(),
                "서버 오류가 발생하였습니다. 관리자에게 문의해주세요.",
                exception.getMessage());
        log.error("Unhandled exception: {}", exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
