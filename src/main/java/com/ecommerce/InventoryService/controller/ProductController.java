package com.ecommerce.InventoryService.controller;

import com.ecommerce.InventoryService.common.ApiResponseBuilder;
import com.ecommerce.InventoryService.dto.CreateProductRequest;
import com.ecommerce.InventoryService.dto.UpdateProductRequest;
import com.ecommerce.InventoryService.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService service;

    @GetMapping
    public ResponseEntity<?> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category) {

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        service.search(keyword, category),
                        "Products fetched",
                        HttpStatus.OK
                )
        );
    }

    @GetMapping("/owner/mine")
    public ResponseEntity<?> mine() {

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        service.getMyProducts(),
                        "Owner products fetched",
                        HttpStatus.OK
                )
        );
    }

    @GetMapping("/{id}/availability")
    public ResponseEntity<?> availability(@PathVariable Long id, @RequestParam Integer quantity) {

        service.validateAvailability(id, quantity);

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        true,
                        "Product is available",
                        HttpStatus.OK
                )
        );
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateProductRequest req) {

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        service.create(req),
                        "Product created",
                        HttpStatus.OK
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        service.get(id),
                        "Product fetched",
                        HttpStatus.OK
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UpdateProductRequest req) {

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        service.update(id, req),
                        "Product updated",
                        HttpStatus.OK
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        null,
                        "Product deleted",
                        HttpStatus.OK
                )
        );
    }
}
