package com.ecommerce.InventoryService.dto;

import lombok.Data;

@Data
public class UpdateProductRequest {

    private String productName;
    private String description;
    private String category;
    private String imageUrl;
    private Double price;
    private Integer quantity;
    private Boolean active;
}
