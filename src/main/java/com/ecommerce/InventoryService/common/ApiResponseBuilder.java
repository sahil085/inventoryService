package com.ecommerce.InventoryService.common;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;

import java.util.List;

public class ApiResponseBuilder {

    public static <T> ApiResponse<T> success(T data, String message, HttpStatus status) {

        return ApiResponse.<T>builder()
                .correlationId(MDC.get(AppConstants.CORRELATION_ID))
                .status(status.value())
                .message(message)
                .data(data)
                .build();
    }

    public static ApiResponse<Object> error(String message, List<String> errors, HttpStatus status) {

        return ApiResponse.builder()
                .correlationId(MDC.get(AppConstants.CORRELATION_ID))
                .status(status.value())
                .message(message)
                .errors(errors)
                .build();
    }
}