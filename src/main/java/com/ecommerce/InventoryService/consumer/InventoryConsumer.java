package com.ecommerce.InventoryService.consumer;

import com.ecommerce.InventoryService.producer.EventPublisher;
import com.ecommerce.InventoryService.service.InventoryService;
import com.ecommerce.eventDTO.OrderEvent;
import com.ecommerce.InventoryService.common.AppConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryConsumer {

    private final InventoryService inventoryService;
    private final EventPublisher publisher;

    // 🔥 STEP 1: HANDLE ORDER CREATED EVENT
    @KafkaListener(topics = "order-created", groupId = "inventory-group")
    public void consume(OrderEvent event) {

        // 🔥 Set correlationId in MDC (VERY IMPORTANT)
        if (event.getCorrelationId() != null) {
            MDC.put(AppConstants.CORRELATION_ID, event.getCorrelationId());
        }

        log.info("Received order-created event for orderId={}", event.getOrderId());

        try {

            boolean success = inventoryService.reserve(event);

            if (success) {

                event.setStatus("INVENTORY_SUCCESS");

                publisher.publish("inventory-success", event);

                log.info("Inventory reserved successfully for orderId={}", event.getOrderId());

            } else {

                event.setStatus("FAILED");

                publisher.publish("inventory-failed", event);

                log.error("Inventory reservation failed for orderId={}", event.getOrderId());
            }

        } catch (Exception ex) {

            log.error("Exception while processing inventory for orderId={}",
                    event.getOrderId(), ex);

            event.setStatus("FAILED");

            publisher.publish("inventory-failed", event);

        } finally {
            MDC.clear();
        }
    }

    // 🔥 STEP 2: HANDLE PAYMENT FAILURE → ROLLBACK
    @KafkaListener(topics = "payment-failed", groupId = "inventory-group")
    public void rollback(OrderEvent event) {

        if (event.getCorrelationId() != null) {
            MDC.put(AppConstants.CORRELATION_ID, event.getCorrelationId());
        }

        log.info("Received payment-failed event → rollback inventory orderId={}",
                event.getOrderId());

        try {

            inventoryService.rollback(event);

            log.info("Inventory rollback completed for orderId={}", event.getOrderId());

        } catch (Exception ex) {

            log.error("Error during rollback for orderId={}",
                    event.getOrderId(), ex);

        } finally {
            MDC.clear();
        }
    }
}