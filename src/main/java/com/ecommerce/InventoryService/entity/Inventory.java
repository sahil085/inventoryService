package com.ecommerce.InventoryService.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventory", indexes = {
        @Index(name = "idx_inventory_product_name", columnList = "productName"),
        @Index(name = "idx_inventory_category", columnList = "category")
})
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inventory extends BaseEntity{

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private Long productId;

    @Column(nullable = false)
    private String productName;

    @Column(length = 1000)
    private String description;

    private String category;

    @Column(length = 2048)
    private String imageUrl;

    @Column(nullable = false)
    private String ownerEmail;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer availableQuantity;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;
}
