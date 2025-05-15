package com.dtt.anms.data.batch.processing.service;

import com.dtt.anms.data.batch.processing.entity.KafkaBatchClick;
import org.apache.kafka.clients.consumer.ConsumerRecords;

public interface ClinckHouseDealService {
    void kDataClinckHouseDeal(ConsumerRecords<String, String> consumerRecords, KafkaBatchClick batchClick);
}
