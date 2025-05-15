package com.dtt.anms.data.batch.processing.job;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.dtt.anms.data.batch.processing.dao.KafkaBatchClickDao;
import com.dtt.anms.data.batch.processing.entity.KafkaBatchClick;
import com.dtt.anms.data.batch.processing.service.BatchDataInputService;
import com.dtt.anms.data.batch.processing.service.KafkaConsumerDealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description: 查询基础配置数据的job
 * @author: zhangbin
 * @create: 2024-05-14 15:37
 **/
@EnableScheduling
@Slf4j
@Component
public class BaseBatchTask {

    //单独获取任务批次id
    @Value(value = "${anms.kftas.id}")
    private long kfId;

    KafkaConsumer<String, String> kafkaConsumer = null;

    @Resource
    BatchDataInputService batchDataInputService;

    @Resource
    KafkaBatchClickDao kafkaBatchClickDao;

    @Resource
    KafkaConsumerDealService kafkaConsumerDealService;

    //@Scheduled(cron = "${anms.task.batch.cron}")
    private void findBatch() {
        //城域网，10秒钟，1万条记录。
        log.info("定时任务扫描开始:" + DateUtil.now());
        batchDataInputService.batchTaskDeal(kafkaConsumer);
        log.info("定时任务扫描结束:" + DateUtil.now());
    }

    //@Scheduled(fixedRate = 1000*60*10000)
    private void createBatch() {
        if (ObjectUtil.isNotNull(kafkaConsumer)){
            kafkaConsumer.close();
        }
        List<KafkaBatchClick> batchClicks = kafkaBatchClickDao.getKfBachCli(kfId);
        KafkaBatchClick kafkaBatchClick = batchClicks.get(0);
        kafkaConsumer = kafkaConsumerDealService.getKafkaConsumer(kafkaBatchClick);
    }

}
