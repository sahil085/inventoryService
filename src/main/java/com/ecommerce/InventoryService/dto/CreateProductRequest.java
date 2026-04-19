package com.ecommerce.InventoryService.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateProductRequest {

    @NotNull
    private Long productId;

    @NotBlank
    private String productName;

    private String description;

    private String category;

    private String imageUrl;

    @NotNull
    private Double price;

    @Min(0)
    private Integer quantity;
}
