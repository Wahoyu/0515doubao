package com.dtt.anms.syslog.processing.listener;

import cn.hutool.core.date.DateUtil;
import com.dtt.anms.syslog.processing.service.ISyslogHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @description: 接收syslog的kafka消息
 * @author: zhangbin
 * @create: 2024-04-15 18:38
 **/
@Component
@Slf4j
public class SyslogKafkaListener {

    @Resource
    ISyslogHandlerService syslogHandlerService;

    @KafkaListener(topics = "#{'${anms.syslog.kafka.consumer.topics}'.split(',')}", groupId = "${anms.syslog.kafka.consumer.groupId}")
    public void listener(ConsumerRecord<?, ?> record, Acknowledgment ack) {
        try {
            //log.info("test:"+records.size());
            //System.out.println("message:"+records.get(0).value());

            Optional<?> kafkaMessage = Optional.ofNullable(record.value());

            if (kafkaMessage.isPresent()) {
                Object message = kafkaMessage.get();

                String value = message.toString();
                log.info("现在的时间:"+ DateUtil.now());
                log.info("原始信息是:" + value);

                syslogHandlerService.handle(value, false);
            }
        }catch (Exception e){
            log.error("SyslogKafkaListener:",e);
        }finally {
            //修改偏移量,修改了之后，重启后，会话设置才会起作用，不会重头读数据
            ack.acknowledge();
        }
    }
}
