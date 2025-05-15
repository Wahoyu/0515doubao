package com.dtt.anms.data.batch.processing.service.impl;

import com.dtt.anms.data.batch.processing.entity.KafkaBatchClick;
import com.dtt.anms.data.batch.processing.service.KafkaConsumerDealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Properties;

/**
 * @description: kafka处理类
 * @author: zhangbin
 * @create: 2024-05-15 09:23
 **/
@Service
@Slf4j
public class KafkaConsumerDealImpl implements KafkaConsumerDealService {

    @Override
    public KafkaConsumer<String, String> getKafkaConsumer(KafkaBatchClick kafkaBatchClick) {

        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBatchClick.getKafkaBootstrapServers());
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 20000);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaBatchClick.getKafkaGroupId());
        //控制消息的提交时间
        properties.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 6000);
        //控制每一个消息的接收大小-4mb
        properties.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, 8388608);

        properties.put("security.protocol", "SASL_PLAINTEXT");
        properties.put("sasl.mechanism", "PLAIN");
        properties.put("sasl.jaas.config", "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"dt_kafka\" password=\"dt_nms_soft\"; ");

        try {
            KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
            return kafkaConsumer;
        } catch (Exception e) {
            log.error("getKafkaConsumer-ex:", e);
            return null;
        }
    }

    @Override
    public long getOffset(KafkaBatchClick kafkaBatchClick, KafkaConsumer<String, String> kafkaConsumer) {
        long latestOffset = 0l;
        try {
            TopicPartition tp = new TopicPartition(kafkaBatchClick.getKafkaTopicName(), 0);
            latestOffset = kafkaConsumer.endOffsets(Arrays.asList(tp)).get(tp);
        } catch (Exception e) {
            log.error("getOffset-ex:", e);
        }
        return latestOffset;
    }
}
