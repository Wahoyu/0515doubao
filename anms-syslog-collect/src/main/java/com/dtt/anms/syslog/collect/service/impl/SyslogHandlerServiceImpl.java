package com.dtt.anms.syslog.collect.service.impl;

import com.dtt.anms.syslog.collect.service.ISyslogHandlerService;
import com.dtt.anms.syslog.collect.service.SyslogMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description:
 * @author: zhangbin
 * @create: 2024-04-15 08:42
 **/
@Service
@Slf4j
public class SyslogHandlerServiceImpl implements ISyslogHandlerService {

    /**
     * 发送消息主题
     */
    @Value(value = "${anms.syslog.kafka.producer.topics}")
    String topicName;

    @Resource
    SyslogMsgService syslogMsgService;

    @Override
    @Async("asyncSyslogExecutor")
    public String handle(String ip, String msg, int port,boolean test) {
        String message = ip+"-_--_-"+msg;
        //数据发送到kafka的topic
        syslogMsgService.send(topicName,message);
        return null;
    }
}
