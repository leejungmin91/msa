package com.store.common.http;


import com.store.common.exception.ApiException;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ApiResponse {

    private String code;
    private String message;
    private Object data;
    private String error;

    public static ApiResponse success() {
        return ApiResponse
                .builder()
                .code(ApiCode.SUCCESS.getCode())
                .message(ApiCode.SUCCESS.getMessage())
                .build();
    }

    public static ApiResponse success(Object data) {
        return ApiResponse
                .builder()
                .code(ApiCode.SUCCESS.getCode())
                .message(ApiCode.SUCCESS.getMessage())
                .data(data)
                .build();
    }

    public static ApiResponse fail() {
        return ApiResponse
                .builder()
                .code(ApiCode.FAIL.getCode())
                .message(ApiCode.FAIL.getMessage())
                .build();
    }


    public static ApiResponse fail(Object data) {
        return ApiResponse
                .builder()
                .code(ApiCode.FAIL.getCode())
                .message(ApiCode.FAIL.getMessage())
                .data(data)
                .build();
    }

    public static void doApiThrow() {
    	throw new ApiException(ApiCode.FAIL);
    }

    public static void doApiThrow(ApiCode resCode) {
    	throw new ApiException(resCode);
    }

}
