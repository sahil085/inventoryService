package com.ecommerce.InventoryService.service;

import com.ecommerce.InventoryService.entity.Inventory;
import com.ecommerce.InventoryService.repository.InventoryRepository;
import com.ecommerce.eventDTO.OrderEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InventoryService {

    private final InventoryRepository repo;

    public boolean reserve(OrderEvent event) {

        log.info("Reserving inventory for orderId={}", event.getOrderId());

        List<OrderEvent.Item> items = event.getItems().stream()
                .sorted(Comparator.comparing(OrderEvent.Item::getProductId))
                .toList();

        for (OrderEvent.Item item : items) {

            Inventory inv = repo.findByProductIdForUpdate(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + item.getProductId()));

            if (Boolean.FALSE.equals(inv.getActive())) {
                log.error("Inactive productId={} requested for orderId={}", item.getProductId(), event.getOrderId());
                return false;
            }

            if (inv.getAvailableQuantity() < item.getQuantity()) {
                log.error("Insufficient stock for productId={}, required={}, available={}",
                        item.getProductId(),
                        item.getQuantity(),
                        inv.getAvailableQuantity());
                return false;
            }

            inv.setAvailableQuantity(inv.getAvailableQuantity() - item.getQuantity());
            repo.save(inv);

            log.info("Reserved productId={}, qty={}", item.getProductId(), item.getQuantity());
        }

        log.info("Inventory reserved successfully for orderId={}", event.getOrderId());
        return true;
    }

    public void rollback(OrderEvent event) {

        log.info("Rolling back inventory for orderId={}", event.getOrderId());

        List<OrderEvent.Item> items = event.getItems().stream()
                .sorted(Comparator.comparing(OrderEvent.Item::getProductId))
                .toList();

        for (OrderEvent.Item item : items) {

            Inventory inv = repo.findByProductIdForUpdate(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found during rollback"));

            inv.setAvailableQuantity(inv.getAvailableQuantity() + item.getQuantity());
            repo.save(inv);

            log.info("Rollback productId={}, qty={}", item.getProductId(), item.getQuantity());
        }

        log.info("Rollback completed for orderId={}", event.getOrderId());
    }
}
