package com.dtt.anms.data.batch.processing.service;

import com.dtt.anms.data.batch.processing.entity.KafkaBatchClick;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public interface KafkaConsumerDealService {

    /**
     * @param kafkaBatchClick
     * @return
     * 根据数据配置返回kafka的consumer信息
     */
    KafkaConsumer<String, String> getKafkaConsumer(KafkaBatchClick kafkaBatchClick);
    /**
     * @param kafkaConsumer
     * @param kafkaBatchClick
     * @return
     * 获取指定topic的当前偏移量
     */
    long getOffset(KafkaBatchClick kafkaBatchClick,KafkaConsumer<String, String> kafkaConsumer);

}
