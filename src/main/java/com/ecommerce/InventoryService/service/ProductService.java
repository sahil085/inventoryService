package com.ecommerce.InventoryService.service;

import com.ecommerce.InventoryService.dto.CreateProductRequest;
import com.ecommerce.InventoryService.dto.ProductResponse;
import com.ecommerce.InventoryService.dto.UpdateProductRequest;
import com.ecommerce.InventoryService.entity.Inventory;
import com.ecommerce.InventoryService.exception.ProductNotFoundException;
import com.ecommerce.InventoryService.exception.UnauthorizedActionException;
import com.ecommerce.InventoryService.repository.InventoryRepository;
import com.ecommerce.InventoryService.util.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final InventoryRepository repo;

    public ProductResponse create(CreateProductRequest req) {

        requireOwnerOrAdmin();
        log.info("Creating product: {}", req.getProductId());

        if (repo.existsByProductId(req.getProductId())) {
            throw new RuntimeException("Product already exists");
        }

        Inventory inv = Inventory.builder()
                .productId(req.getProductId())
                .productName(req.getProductName())
                .description(req.getDescription())
                .category(req.getCategory())
                .imageUrl(req.getImageUrl())
                .ownerEmail(UserContext.getUser())
                .price(req.getPrice())
                .availableQuantity(req.getQuantity())
                .active(true)
                .build();

        repo.save(inv);

        return map(inv);
    }

    public ProductResponse get(Long productId) {

        log.info("Fetching product: {}", productId);

        Inventory inv = repo.findByProductId(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        return map(inv);
    }

    public List<ProductResponse> getAll() {

        log.info("Fetching all products");

        return repo.findVisibleProducts()
                .stream()
                .map(this::map)
                .toList();
    }

    public List<ProductResponse> getMyProducts() {
        requireOwnerOrAdmin();
        return repo.findByOwnerEmailOrderByProductNameAsc(UserContext.getUser())
                .stream()
                .map(this::map)
                .toList();
    }

    public void validateAvailability(Long productId, Integer quantity) {
        if (quantity == null || quantity < 1) {
            throw new RuntimeException("Quantity must be at least 1");
        }

        Inventory inv = repo.findByProductId(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        if (Boolean.FALSE.equals(inv.getActive())) {
            throw new RuntimeException("Product is not active");
        }

        if (inv.getAvailableQuantity() < quantity) {
            throw new RuntimeException("Only " + inv.getAvailableQuantity() + " product(s) available");
        }
    }

    public List<ProductResponse> search(String keyword, String category) {

        log.info("Searching products keyword={}, category={}", keyword, category);

        List<Inventory> products;

        if (StringUtils.hasText(keyword)) {
            products = repo.findVisibleProductsByKeyword(keyword);
        } else if (StringUtils.hasText(category)) {
            products = repo.findVisibleProductsByCategory(category);
        } else {
            products = repo.findVisibleProducts();
        }

        return products.stream().map(this::map).toList();
    }

    public ProductResponse update(Long productId, UpdateProductRequest req) {

        requireOwnerOrAdmin();
        log.info("Updating product: {}", productId);

        Inventory inv = repo.findByProductId(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        requireOwnerOfProduct(inv);

        if (req.getProductName() != null)
            inv.setProductName(req.getProductName());

        if (req.getDescription() != null)
            inv.setDescription(req.getDescription());

        if (req.getCategory() != null)
            inv.setCategory(req.getCategory());

        if (req.getImageUrl() != null)
            inv.setImageUrl(req.getImageUrl());

        if (req.getPrice() != null)
            inv.setPrice(req.getPrice());

        if (req.getQuantity() != null)
            inv.setAvailableQuantity(req.getQuantity());

        if (req.getActive() != null)
            inv.setActive(req.getActive());

        repo.save(inv);

        return map(inv);
    }

    public void delete(Long productId) {

        requireOwnerOrAdmin();
        log.info("Deactivating product: {}", productId);

        Inventory inv = repo.findByProductId(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        requireOwnerOfProduct(inv);

        inv.setActive(false);
        repo.save(inv);
    }

    private ProductResponse map(Inventory inv) {
        return ProductResponse.builder()
                .productId(inv.getProductId())
                .productName(inv.getProductName())
                .description(inv.getDescription())
                .category(inv.getCategory())
                .imageUrl(inv.getImageUrl())
                .ownerEmail(inv.getOwnerEmail())
                .price(inv.getPrice())
                .availableQuantity(inv.getAvailableQuantity())
                .active(inv.getActive())
                .build();
    }

    private void requireOwnerOrAdmin() {
        String role = UserContext.getRole();
        if (!"ROLE_STORE_OWNER".equals(role) && !"ROLE_ADMIN".equals(role)) {
            throw new UnauthorizedActionException("Only store owners or admins can manage products");
        }
    }

    private void requireOwnerOfProduct(Inventory inv) {
        if ("ROLE_ADMIN".equals(UserContext.getRole())) {
            return;
        }
        if (!UserContext.getUser().equals(inv.getOwnerEmail())) {
            throw new UnauthorizedActionException("Store owners can manage only their own products");
        }
    }
}
