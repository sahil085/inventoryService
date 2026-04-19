package com.ecommerce.InventoryService.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponse {

    private Long productId;
    private String productName;
    private String description;
    private String category;
    private String imageUrl;
    private String ownerEmail;
    private Double price;
    private Integer availableQuantity;
    private Boolean active;
}
