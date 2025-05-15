package com.dtt.anms.data.batch.processing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
@Slf4j
public class DataBatchProcessing {
    public static void main(String[] args) {
        SpringApplication.run(DataBatchProcessing.class, args);
        log.info("--批处理kakfa数据插入clinckhouse程序启动成功--");
    }
}
