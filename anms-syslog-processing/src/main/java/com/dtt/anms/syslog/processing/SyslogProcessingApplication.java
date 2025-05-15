package com.dtt.anms.syslog.processing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
@Slf4j
public class SyslogProcessingApplication {
    public static void main(String[] args) {
        SpringApplication.run(SyslogProcessingApplication.class, args);
        log.info("--接入网Syslog告警启动成功--");
    }
}
