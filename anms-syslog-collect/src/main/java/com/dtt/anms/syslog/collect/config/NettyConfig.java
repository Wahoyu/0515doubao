package com.dtt.anms.syslog.collect.config;


import com.dtt.anms.syslog.collect.service.INettyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
@Order(3)
public class NettyConfig implements CommandLineRunner {

    @Resource
    INettyService nettyService;

    @Override
    public void run(String... args) throws Exception {
        nettyService.start();
    }
}