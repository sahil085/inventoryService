package com.ecommerce.InventoryService.exception;

import com.ecommerce.InventoryService.common.ApiResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handle(RuntimeException ex) {

        return ResponseEntity.badRequest()
                .body(ApiResponseBuilder.error(
                        ex.getMessage(),
                        List.of(),
                        HttpStatus.BAD_REQUEST
                ));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<?> handle(ProductNotFoundException ex) {

        return ResponseEntity.badRequest()
                .body(ApiResponseBuilder.error(
                        ex.getMessage(),
                        List.of(),
                        HttpStatus.NOT_FOUND
                ));
    }

    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<?> handle(UnauthorizedActionException ex) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponseBuilder.error(
                        ex.getMessage(),
                        List.of(),
                        HttpStatus.FORBIDDEN
                ));
    }
}
