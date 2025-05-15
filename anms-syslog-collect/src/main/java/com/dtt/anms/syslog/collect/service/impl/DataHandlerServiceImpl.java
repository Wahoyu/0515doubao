package com.dtt.anms.syslog.collect.service.impl;


import com.dtt.anms.syslog.collect.service.IDataHandlerService;
import com.dtt.anms.syslog.collect.service.ISyslogHandlerService;
import com.dtt.anms.syslog.collect.service.SyslogMsgService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.InetSocketAddress;

@ChannelHandler.Sharable
@Service
@Slf4j
public class DataHandlerServiceImpl extends SimpleChannelInboundHandler<DatagramPacket> implements IDataHandlerService {

    @Resource
    ISyslogHandlerService syslogHandlerService;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {
        //网络地址
        InetSocketAddress remoteAddress = datagramPacket.sender();
        String ip = remoteAddress.getAddress().getHostAddress();
        int port = remoteAddress.getPort();

        //数据内容
        int dataLength = datagramPacket.content().readableBytes();
        byte[] data = new byte[dataLength];
        datagramPacket.content().readBytes(data);
        String msg = new String(data);
        log.info(msg);

        syslogHandlerService.handle(ip,msg,port,false);
    }


}
