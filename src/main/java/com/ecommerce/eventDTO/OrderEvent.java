package com.ecommerce.eventDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent {

    private Long orderId;
    private String user;
    private List<Item> items;
    private String status;
    private String correlationId;
    private BigDecimal totalAmount;
    private Long amountCents;
    private String currency;
    private String paymentId;
    private String paymentUrl;
    private String stripeCheckoutSessionId;
    private String stripePaymentIntentId;
    private String failureReason;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {
        private Long productId;
        private Integer quantity;
    }
}
