package com.ecommerce.InventoryService.repository;

import com.ecommerce.InventoryService.entity.Inventory;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByProductId(Long productId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from Inventory i where i.productId = :productId")
    Optional<Inventory> findByProductIdForUpdate(@Param("productId") Long productId);

    boolean existsByProductId(Long productId);

    @Query("select i from Inventory i where i.active = true or i.active is null order by i.productName asc")
    List<Inventory> findVisibleProducts();

    @Query("select i from Inventory i where (i.active = true or i.active is null) and lower(i.category) = lower(:category) order by i.productName asc")
    List<Inventory> findVisibleProductsByCategory(@Param("category") String category);

    @Query("select i from Inventory i where (i.active = true or i.active is null) and lower(i.productName) like lower(concat('%', :keyword, '%')) order by i.productName asc")
    List<Inventory> findVisibleProductsByKeyword(@Param("keyword") String keyword);

    List<Inventory> findByOwnerEmailOrderByProductNameAsc(String ownerEmail);
}
