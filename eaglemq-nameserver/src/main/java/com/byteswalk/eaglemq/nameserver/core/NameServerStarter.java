package com.byteswalk.eaglemq.nameserver.core;

import cn.byteswalk.eaglemq.common.coder.TcpMsgDecoder;
import cn.byteswalk.eaglemq.common.coder.TcpMsgEncoder;
import com.byteswalk.eaglemq.nameserver.event.EventBus;
import com.byteswalk.eaglemq.nameserver.handler.TcpNettyServerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-05 13:04
 * @Description: 注册中心引导类
 * @Version: 1.0
 */
public class NameServerStarter {

    private final Logger logger = LoggerFactory.getLogger(NameServerStarter.class);

    private final int port;

    public NameServerStarter(int port) {
        this.port = port;
    }

    public void startServer() throws InterruptedException {
        //构建netty服务
        //注入编解码器
        //注入特定的handler
        //启动netty服务

        //处理网络io中的accept事件
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        //处理网络io中的read&write事件
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup,workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childHandler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ch.pipeline().addLast(new TcpMsgDecoder());
                ch.pipeline().addLast(new TcpMsgEncoder());
                ch.pipeline().addLast(new TcpNettyServerHandler(new EventBus("broker-connection-")));
            }
        });
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            logger.info("注册中心(NameServer)已经关闭");
        }));
        ChannelFuture channelFuture = bootstrap.bind(port).sync();
        logger.info("注册中心(NameServer)启动成功，端口是：{}", port);
        // 阻塞代码
        channelFuture.channel().closeFuture().sync();
    }
}
