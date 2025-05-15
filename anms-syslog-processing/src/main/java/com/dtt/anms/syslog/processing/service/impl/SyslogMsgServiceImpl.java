package com.dtt.anms.syslog.processing.service.impl;


import com.dtt.anms.syslog.processing.service.SyslogMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.Resource;

/**
 * @description:
 * @author: zhangbin
 * @create: 2024-04-15 08:17
 **/
@Service
@Slf4j
public class SyslogMsgServiceImpl implements SyslogMsgService {

    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void send(String topicName, String message) {
        //发送消息
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topicName, message);
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {

            @Override
            public void onFailure(Throwable throwable) {
                //发送失败的处理
                log.error(topicName + " - syslog-生产者 发送消息失败：" + throwable.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
                //成功的处理
                log.info(topicName + " - syslog-生产者 发送消息成功：" + stringObjectSendResult.toString());
            }
        });
    }
}
