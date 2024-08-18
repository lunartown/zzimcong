package com.zzimcong.product.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class KafkaProducer<T> {
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

    private final KafkaTemplate<String, T> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, T> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String topic, T payload) {
        logger.info("Sending Kafka message to topic: {} with payload: {}", topic, payload);
        CompletableFuture<SendResult<String, T>> future = kafkaTemplate.send(topic, payload);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                logger.info("Message sent successfully to topic: {}", topic);
            } else {
                logger.error("Failed to send message to topic: {}", topic, ex);
            }
        });
    }
}