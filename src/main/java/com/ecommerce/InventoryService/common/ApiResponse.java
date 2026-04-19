package com.ecommerce.InventoryService.common;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private String correlationId;
    private int status;
    private String message;
    private T data;
    private List<String> errors;
}