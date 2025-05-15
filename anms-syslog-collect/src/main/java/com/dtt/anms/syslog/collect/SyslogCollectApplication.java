package com.dtt.anms.syslog.collect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

//@EnableDiscoveryClient
@SpringBootApplication
@Slf4j
public class SyslogCollectApplication {
    public static void main(String[] args) {
        SpringApplication.run(SyslogCollectApplication.class, args);
        log.info("--Syslog数据接收程序启动成功--");
    }
}
