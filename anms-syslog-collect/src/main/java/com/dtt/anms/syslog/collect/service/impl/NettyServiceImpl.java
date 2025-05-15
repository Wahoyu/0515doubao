package com.dtt.anms.syslog.collect.service.impl;


import com.dtt.anms.syslog.collect.service.IDataHandlerService;
import com.dtt.anms.syslog.collect.service.INettyService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class NettyServiceImpl implements INettyService {

    @Value(value = "${anms.syslog.port}")
    private int port;

    @Resource
    IDataHandlerService dataHandlerService;

    @Override
    public void start() {
        log.info("---------------启动 Syslog 开始-------------");

        if (Epoll.isAvailable()) {
            epoll();
        } else {
            nio();
        }
    }

    public void epoll() {
        log.info("---------------启动 Syslog Epoll 开始-------------");
        EpollEventLoopGroup group = new EpollEventLoopGroup();//NioEventLoopGroup ->EpollEventLoopGroup
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(EpollDatagramChannel.class) // NioServerSocketChannel -> EpollDatagramChannel
                    .option(ChannelOption.SO_BROADCAST, true)
                    .option(EpollChannelOption.SO_REUSEPORT, true) // 配置EpollChannelOption.SO_REUSEPORT
                    .option(ChannelOption.SO_RCVBUF, 1024 * 1024 * 500)
                    .handler(dataHandlerService);

            // linux系统下使用SO_REUSEPORT特性，使得多个线程绑定同一个端口
            int cpuNum = Runtime.getRuntime().availableProcessors();
            log.info("\n---------------------------------------\nusing epoll reuseport and cpu:" + cpuNum);
            for (int i = 0; i < cpuNum; i++) {
                ChannelFuture future = bootstrap.bind(port).await();
                if (!future.isSuccess()) {
                    throw new Exception(String.format("Fail to bind on [port = %d].", port), future.cause());
                }
            }
        } catch (Exception e) {
            log.error("---------------启动 Syslog 异常-------------", e);
        }
        log.info("---------------启动 Syslog 成功-------------");
    }

    public void nio() {
        log.info("---------------启动 Syslog NIO 开始-------------");
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();

             bootstrap.group(group)
                     .channel(NioDatagramChannel.class)
                     .option(ChannelOption.SO_BROADCAST, true)
                     .option(ChannelOption.SO_RCVBUF, 1024 * 1024 * 500)
                    .handler(this.dataHandlerService);
            Channel channel = bootstrap.bind(this.port).sync().channel();
            log.info("---------------启动 Syslog NIO 成功-------------");
            channel.closeFuture().await();
        } catch (Exception e) {
            log.error("---------------启动 Syslog NIO 异常-------------", e);
        } finally {
            log.info("---------------停止 Syslog NIO 成功-------------");
            group.shutdownGracefully();
        }
    }
}
