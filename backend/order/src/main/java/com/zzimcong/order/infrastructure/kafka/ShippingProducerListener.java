package com.zzimcong.order.infrastructure.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.ProducerListener;

public class ShippingProducerListener implements ProducerListener<String, Object> {

    private static final Logger logger = LoggerFactory.getLogger(ShippingProducerListener.class);

    @Override
    public void onSuccess(ProducerRecord<String, Object> producerRecord, RecordMetadata recordMetadata) {
        logger.info("Message sent successfully to topic {} partition {} offset {}",
                recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset());
    }

    @Override
    public void onError(ProducerRecord<String, Object> producerRecord, RecordMetadata recordMetadata, Exception exception) {
        logger.error("Error sending message to topic {} partition {}",
                producerRecord.topic(), producerRecord.partition(), exception);
    }
}