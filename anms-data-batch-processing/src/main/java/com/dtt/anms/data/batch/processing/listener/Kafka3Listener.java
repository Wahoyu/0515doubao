package com.dtt.anms.data.batch.processing.listener;

import cn.hutool.core.util.ObjectUtil;
import com.dtt.anms.data.batch.processing.dao.KafkaBatchClickDao;
import com.dtt.anms.data.batch.processing.entity.KafkaBatchClick;
import com.dtt.anms.data.batch.processing.service.ClinckHouseDealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @description: 处理syslog告警
 * @author: zhangbin
 * @create: 2024-06-26 10:52
 **/
@Component
@Slf4j
public class Kafka3Listener {

    @Resource
    ClinckHouseDealService clinckHouseDealService;

    @Resource
    KafkaBatchClickDao kafkaBatchClickDao;

    KafkaBatchClick kafkaBatchClick;

    @Value(value = "${anms.task.kafka.kafka3.configId}")
    int configId;
    @Value(value = "${anms.task.kafka.kafka3.sleepTime}")
    int sleepTimp;

    @KafkaListener(topics = "#{'${anms.task.kafka.kafka3.topics}'.split(',')}", groupId = "${anms.task.kafka.kafka3.groupId}", autoStartup = "${anms.task.kafka.kafka3.isStart}")
    public void receive(ConsumerRecords<String, String> consumerRecords, Acknowledgment ack) {
        try {
           if (ObjectUtil.isNull(kafkaBatchClick) || ObjectUtil.isEmpty(kafkaBatchClick)) {
                kafkaBatchClick = kafkaBatchClickDao.getKfBachCliSingle(configId);
                log.info("kafka3-首次初始化数据库配置:" + kafkaBatchClick.toString());
            }
            log.info("topic:{},收到的数据量为:{}",kafkaBatchClick.getKafkaTopicName(),consumerRecords.count());
            clinckHouseDealService.kDataClinckHouseDeal(consumerRecords, kafkaBatchClick);
            if (consumerRecords.count() > 0) {
                ack.acknowledge();
                Thread.sleep(sleepTimp);
            }
        } catch (Exception e) {
            log.error("kafka3- 接收数据错误：", e);
        }
    }
}
