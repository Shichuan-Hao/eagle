package cn.byteswalk.eaglemq.nameserver.core;

import cn.byteswalk.eaglemq.common.coder.TcpMsgDecoder;
import cn.byteswalk.eaglemq.common.coder.TcpMsgEncoder;
import cn.byteswalk.eaglemq.nameserver.event.EventBus;
import cn.byteswalk.eaglemq.nameserver.handler.TcpNettyServerHandler;

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
        logger.info("{} Starting NameServer...", NameServerStarter.class.getSimpleName());
        // 构建 netty 服务
        // 注入编解码器
        // 注入特定的handler
        // 启动netty服务

        // 1. 声明线程池
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        // 2. 服务端引导器
        ServerBootstrap bootstrap = new ServerBootstrap();
        // 3. 设置线程池
        bootstrap.group(bossGroup, workerGroup);
        // 4. 设置 ServerSocketChannel类型，设置 Netty 程序以什么样的 IO 模型运行
        bootstrap.channel(NioServerSocketChannel.class);

        // 5. 设置 SocketCHandler 对应的 Handler
        bootstrap.childHandler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                channel.pipeline().addLast(new TcpMsgDecoder());
                channel.pipeline().addLast(new TcpMsgEncoder());
                channel.pipeline().addLast(new TcpNettyServerHandler(new EventBus()));
            }
        });
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // 10. 优雅地关闭两个线程池
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
//            logger.info("\33[32;4mnameserver is closed\33[0;4m");
            System.out.println("nameserver is closed");
        }));
        // 8. 绑定端口
        ChannelFuture channelFuture = bootstrap.bind(port).sync();
        logger.info("\33[32;4mstart nameserver application on port: {}\33[0;4m", port);
        // 9. 等待服务端监听端口关闭，这里会阻塞主线程
        channelFuture.channel().closeFuture().sync();
    }
}

