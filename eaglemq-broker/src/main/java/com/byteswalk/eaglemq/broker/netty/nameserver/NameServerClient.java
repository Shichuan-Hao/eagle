package com.byteswalk.eaglemq.broker.netty.nameserver;

import com.alibaba.fastjson.JSON;
import com.byteswalk.eaglemq.broker.cache.CommonCache;
import com.byteswalk.eaglemq.broker.config.GlobalProperties;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.internal.StringUtil;
import cn.byteswalk.eaglemq.common.coder.TcpMsg;
import cn.byteswalk.eaglemq.common.coder.TcpMsgDecoder;
import cn.byteswalk.eaglemq.common.coder.TcpMsgEncoder;
import cn.byteswalk.eaglemq.common.dto.RegistryDTO;
import cn.byteswalk.eaglemq.common.enums.NameServerEventCode;

import java.net.Inet4Address;
import java.net.UnknownHostException;

/**
 * @Author idea
 * @Date: Created in 08:32 2024/5/7
 * @Description 服务与nameserver服务端创建长链接，支持链接创建，支持重试机制
 */
public class NameServerClient {


    private final EventLoopGroup clientGroup = new NioEventLoopGroup();
    private final Bootstrap bootstrap = new Bootstrap();
    private Channel channel;

    /**
     * 初始化链接
     */
    public void initConnection() {
        String ip = CommonCache.getGlobalProperties().getNameserverIp();
        Integer port = CommonCache.getGlobalProperties().getNameserverPort();
        if (StringUtil.isNullOrEmpty(ip) || port == null || port < 0) {
            throw new RuntimeException("error port or ip");
        }
        bootstrap.group(clientGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new TcpMsgDecoder());
                ch.pipeline().addLast(new TcpMsgEncoder());
                ch.pipeline().addLast(new NameServerRespChannelHandler());
            }
        });
        ChannelFuture channelFuture = null;
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            clientGroup.shutdownGracefully();
            System.out.println("nameserver client is closed");
        }));
        try {
            channelFuture = bootstrap.connect(ip, port).sync();
            channel = channelFuture.channel();
            System.out.println("success connected to nameserver!");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Channel getChannel() {
        if (channel == null) {
            throw new RuntimeException("channel has not been connected!");
        }
        return channel;
    }

    public void sendRegistryMsg() {
        RegistryDTO registryDTO = new RegistryDTO();
        try {
            registryDTO.setBrokerIp(Inet4Address.getLocalHost().getHostAddress());
            GlobalProperties globalProperties = CommonCache.getGlobalProperties();
            registryDTO.setBrokerPort(globalProperties.getBrokerPort());
            registryDTO.setUser(globalProperties.getNameserverUser());
            registryDTO.setPassword(globalProperties.getNameserverPassword());
            registryDTO.setTimeStamp(System.currentTimeMillis());
            byte[] body = JSON.toJSONBytes(registryDTO);
            TcpMsg tcpMsg = new TcpMsg(NameServerEventCode.REGISTRY.getCode(), body);
            channel.writeAndFlush(tcpMsg);
            System.out.println("发送注册事件");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

    }
}
