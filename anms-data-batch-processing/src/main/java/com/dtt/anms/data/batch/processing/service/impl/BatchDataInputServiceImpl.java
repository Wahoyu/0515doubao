package com.dtt.anms.data.batch.processing.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.dtt.anms.data.batch.processing.dao.KafkaBatchClickDao;
import com.dtt.anms.data.batch.processing.entity.KafkaBatchClick;
import com.dtt.anms.data.batch.processing.service.BatchDataInputService;
import com.dtt.anms.data.batch.processing.service.ClinckHouseDealService;
import com.dtt.anms.data.batch.processing.service.KafkaConsumerDealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @description: 获取任务信息
 * @author: zhangbin
 * @create: 2024-05-14 17:46
 **/
@Service
@Slf4j
public class BatchDataInputServiceImpl implements BatchDataInputService {

    @Value(value = "${anms.kftas.form}")
    private int idFrom;

    @Resource
    KafkaConsumerDealService kafkaConsumerDealService;

    @Resource
    KafkaBatchClickDao kafkaBatchClickDao;

    @Resource
    ClinckHouseDealService clinckHouseDealService;

    @Override
    public void batchTaskDeal(KafkaConsumer<String, String> kafkaConsumer) {

        List<KafkaBatchClick> batchClicks = kafkaBatchClickDao.getKfBachCli(idFrom);
        for (KafkaBatchClick batchClick : batchClicks) {
            try {

                if (ObjectUtil.isNull(kafkaConsumer) || ObjectUtil.isEmpty(kafkaConsumer)) {
                    kafkaConsumer = kafkaConsumerDealService.getKafkaConsumer(batchClick);
                }
                long nowOffset = kafkaConsumerDealService.getOffset(batchClick, kafkaConsumer);
                log.info("会话:{},最新的偏移量:{}", batchClick.getKafkaTopicName(), nowOffset - 1);
                log.info("会话:{},历史偏移量:{},准备从偏移量:{}读取数据", batchClick.getKafkaTopicName(), batchClick.getTaskHisNum(), batchClick.getTaskHisNum() + 1);
                if (isReachCondition(batchClick, nowOffset - 1)) {
                    TopicPartition tp = new TopicPartition(batchClick.getKafkaTopicName(), 0);
                    kafkaConsumer.assign(Collections.singleton(tp));
                    kafkaConsumer.seek(tp, batchClick.getTaskHisNum() + 1);
                    //满足执行条件，进行数据读取
                    ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofSeconds(20));
                    log.info("会话:{},读取的实际长度为:{}", batchClick.getKafkaTopicName(), consumerRecords.count());
                    recordDeal(consumerRecords, batchClick);
                }else {
                    justUpdateDealTime(batchClick);
                }
                Thread.sleep(1000);
            } catch (Exception e) {
                log.error("查询设备信息异常:{},-ex:{}", batchClick.getTaskBatchId(), e);
            }
        }
        //kafkaConsumer.close();

    }

    /**
     * @param kafkaBatchClick
     * @return 判断该次自动任务执行，是否满足读取kafka数据的条件
     */
    public boolean isReachCondition(KafkaBatchClick kafkaBatchClick, long nowOffset) {
        boolean flag = false;
        //判断当前进程是否在运行中。
        if (kafkaBatchClick.getRunStatus() == 1) {
            log.info("任务:{},上次还没有执行完成,请等待下次扫描", kafkaBatchClick.getTaskBatchName());
            return false;
        }

        if (kafkaBatchClick.getTaskRangeNum() != 0 || kafkaBatchClick.getTaskRangeTime() != 0) {

            //表示条数和时间均配置，符合一种即可
            if (nowOffset > kafkaBatchClick.getTaskHisNum() + kafkaBatchClick.getTaskRangeNum()) {
                log.info("==偏移量满足要求==任务:{},偏移量:{}", kafkaBatchClick.getTaskBatchName(), nowOffset);
                flag = true;
            } else if (nowOffset > kafkaBatchClick.getTaskHisNum() && (new Date().getTime() >= kafkaBatchClick.getTaskHisTime().getTime() + kafkaBatchClick.getTaskRangeTime() * 1000)) {
                log.info("==时间满足要求==任务:{},当前时间:{}", kafkaBatchClick.getTaskBatchName(), DateUtil.now());
                flag = true;
            } else {
                log.info("==时间与偏移量不满足要求==任务:{},id:{},等待下次执行:{}", kafkaBatchClick.getTaskBatchName(), kafkaBatchClick.getTaskBatchId(), DateUtil.now());
                flag = false;
            }

        } else {
            log.info("没有配置时间和条数的匹配项,请在数据库配置:" + kafkaBatchClick.getTaskBatchName());
        }
        return flag;
    }

    /**
     * @param consumerRecords 当满足条件后，进行数据读取。
     */
    public void recordDeal(ConsumerRecords<String, String> consumerRecords, KafkaBatchClick batchClick) {
        try {
            //满足运行条件后，将运行状态改成1,表示在运行中
            String sql = "update kafka_batch_click set run_status = 1 where task_batch_id = " + batchClick.getTaskBatchId();
            kafkaBatchClickDao.updateKfBatch(sql);
            //拿到原始数据后，下面开始对原始数据解析，处理clickhouse的相关逻辑。
            clinckHouseDealService.kDataClinckHouseDeal(consumerRecords, batchClick);
        } catch (Exception e) {
            log.error("recordDeal-ex:", e);
        }
    }

    /**
     * @param batchClick
     * 当不满足数据插入条件时，只更新采集时间
     */
    public void justUpdateDealTime(KafkaBatchClick batchClick){
        String endSql = "update kafka_batch_click set update_time = NOW()  where task_batch_id = " + batchClick.getTaskBatchId();
        kafkaBatchClickDao.updateKfBatch(endSql);
    }
}
