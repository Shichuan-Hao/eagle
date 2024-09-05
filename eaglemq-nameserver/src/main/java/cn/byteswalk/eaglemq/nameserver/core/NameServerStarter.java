package cn.byteswalk.eaglemq.nameserver.core;

import cn.byteswalk.eaglemq.common.coder.TcpMsgDecoder;
import cn.byteswalk.eaglemq.common.coder.TcpMsgEncoder;
import cn.byteswalk.eaglemq.nameserver.handler.TcpNettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-05 13:04
 * @Description:
 * @Version: 1.0
 */
public class NameServerStarter {

    private int port;

    public NameServerStarter(int port) {
        this.port = port;
    }

    public void startServer() throws InterruptedException {
        // 构建 netty 服务
        // 注入编解码器
        // 注入特定的handler
        // 启动netty服务

        // 处理网络io中的accept事件
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        // 处理网络io中的read&write事件
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childHandler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                channel.pipeline().addLast(new TcpMsgEncoder());
                channel.pipeline().addLast(new TcpMsgDecoder());
                channel.pipeline().addLast(new TcpNettyServerHandler());
            }
        });
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }));
        ChannelFuture channelFuture = bootstrap.bind(port).sync();
        channelFuture.channel().closeFuture().sync();

    }
}

