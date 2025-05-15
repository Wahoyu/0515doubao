package com.dtt.anms.syslog.processing.service;

public interface SyslogMsgService {
    /**
     * 发送消息到指定的kafka消息中
     *
     * @param message
     */
    void send(String topicName, String message);


}
