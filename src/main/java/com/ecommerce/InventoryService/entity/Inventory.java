package com.ecommerce.InventoryService.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventory")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private Long productId;

    private Integer availableQuantity;
}