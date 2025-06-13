package com.store.common.exception;

import com.store.common.http.ApiCode;
import com.store.common.http.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@Slf4j
@RestControllerAdvice
public class ApiExceptionAdvice {

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ApiResponse> exceptionHandler(final Exception e) {

        log.info("Exception Advice (Exception)");

        e.printStackTrace();

        return ResponseEntity
                .status(ApiCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(ApiResponse.builder()
                        .code(ApiCode.INTERNAL_SERVER_ERROR.getCode())
                        .message(ApiCode.INTERNAL_SERVER_ERROR.getMessage())
                        .error(e.getMessage())
                        .build());
    }

    @ExceptionHandler({ApiException.class})
    public ResponseEntity<ApiResponse> exceptionHandler(final ApiException e) {
        return ResponseEntity
                .status(e.getApiCode().getStatus())
                .body(ApiResponse.builder()
                        .code(e.getApiCode().getCode())
                        .message(e.getApiCode().getMessage())
                        .build());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ApiResponse> exceptionHandler(final MethodArgumentNotValidException e) {
        ObjectError objectError = e.getBindingResult().getAllErrors().stream().findFirst().orElseThrow();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.builder()
                        .code(ApiCode.BAD_REQUEST.getCode())
                        .message(objectError.getDefaultMessage())
                        .error(e.getMessage())
                        .build());
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ApiResponse> exceptionHandler(final RuntimeException e) {

        e.printStackTrace();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.builder()
                        .code("RUNTIME_EXCEPTION")
                        .message(e.getMessage())
                        .error(e.getMessage())
                        .build());
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ApiResponse> exceptionHandler(final AccessDeniedException e) {

        log.info("Exception Advice (AccessDeniedException)");

        return ResponseEntity
                .status(ApiCode.ACCESS_DENIED.getStatus())
                .body(ApiResponse.builder()
                        .code(ApiCode.ACCESS_DENIED.getCode())
                        .message(ApiCode.ACCESS_DENIED.getMessage())
                        .error(e.getMessage())
                        .build());
    }

}
