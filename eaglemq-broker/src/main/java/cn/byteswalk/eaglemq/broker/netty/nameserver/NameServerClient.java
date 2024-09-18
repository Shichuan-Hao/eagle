package cn.byteswalk.eaglemq.broker.netty.nameserver;

import cn.byteswalk.eaglemq.broker.cache.CommonCache;
import cn.byteswalk.eaglemq.common.coder.TcpMsgDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-13 17:05
 * @Description: 服务与nameserver服务端创建长链接，支持链接创建，支持重试机制
 * @Version: 1.0
 */
public class NameServerClient {

    private final Logger logger = LoggerFactory.getLogger(NameServerClient.class);

    private EventLoopGroup  clientGroup = new NioEventLoopGroup();
    private Bootstrap bootstrap = new Bootstrap();
    private Channel channel;
//    private String DEFAULT_HOST = "127.0.0.1";

    public void initConnection() {
        String ip                 = CommonCache.getGlobalProperties().getNameserverIp();
        Integer port              = CommonCache.getGlobalProperties().getNameserverPort();
//        String nameserverUser     = CommonCache.getGlobalProperties().getNameserverUser();
//        String nameserverPassword = CommonCache.getGlobalProperties().getNameserverPassword();
        if (StringUtil.isNullOrEmpty(ip) || port == null || port < 0) {
            throw new RuntimeException("error port or ip");
        }
        bootstrap.group(clientGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel)
                    throws Exception {
                socketChannel.pipeline().addLast(new TcpMsgDecoder());
                socketChannel.pipeline().addLast(new TcpMsgDecoder());
                socketChannel.pipeline().addLast(new NameServerRespChannelHandler());
            }
        });

        try {
            ChannelFuture channelFuture = bootstrap.connect(ip, port).sync();
            channel = channelFuture.channel();
            logger.info("33[32;4m成功连接到注册中心Nameserver\33[0m");

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取channel通道
     * @return 调用完初始化连接 initConnection()之后再返回通道channel的值
     */
    public Channel getChannel() {
        if (Objects.isNull(channel)) {
            throw new RuntimeException("通道未连接 channel has not been connected!");
        }
        return channel;
    }
}

