package com.dtt.anms.data.batch.processing.service;

import org.apache.kafka.clients.consumer.KafkaConsumer;

public interface BatchDataInputService {
    void batchTaskDeal(KafkaConsumer<String, String> kafkaConsumer);
}
