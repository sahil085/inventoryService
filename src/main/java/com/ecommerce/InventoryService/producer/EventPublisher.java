package com.ecommerce.InventoryService.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publish(String topic, Object event) {

        String correlationId = MDC.get("X-Correlation-Id");

        log.info("Publishing event to topic={} with correlationId={} payload={}",
                topic, correlationId, event);

        kafkaTemplate.send(topic, event);
    }
}