package cn.byteswalk.eaglemq.broker.netty.nameserver;

import cn.byteswalk.eaglemq.broker.cache.CommonCache;
import cn.byteswalk.eaglemq.broker.config.GlobalProperties;
import cn.byteswalk.eaglemq.common.coder.TcpMsg;
import cn.byteswalk.eaglemq.common.coder.TcpMsgDecoder;
import cn.byteswalk.eaglemq.common.dto.RegistryDTO;
import cn.byteswalk.eaglemq.common.enums.NameServerEventCode;

import com.alibaba.fastjson.JSON;

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

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Objects;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-13 17:05
 * @Description: 服务与nameserver服务端创建长链接，支持链接创建，支持重试机制
 * @Version: 1.0
 */
public class NameServerClient {

    private final Logger logger = LoggerFactory.getLogger(NameServerClient.class);

    private final EventLoopGroup  clientGroup = new NioEventLoopGroup();
    private final Bootstrap bootstrap = new Bootstrap();
    private Channel channel;

    public void initConnection() {
        String ip     = CommonCache.getGlobalProperties().getNameserverIp();
        Integer port  = CommonCache.getGlobalProperties().getNameserverPort();

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
        ChannelFuture channelFuture = null;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            TcpMsg tcpMsg = new TcpMsg(NameServerEventCode.UN_REGISTRY.getCode(), new byte[0]);
            channel.writeAndFlush(tcpMsg);
            clientGroup.shutdownGracefully();
            logger.info("nameserver client is closed");
        }));
        try {

            channelFuture = bootstrap.connect(ip, port).sync();
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
        if (channel == null) {
            throw new RuntimeException("通道未连接 channel has not been connected!");
        }
        return channel;
    }

    /**
     * 发送注册事件通知
     */
    public void sendRegistry() {
       String brokerIp = null;
        try {
            brokerIp = Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        RegistryDTO registryDTO = this.buildRegistryDTO(brokerIp);
        byte[] body = JSON.toJSONBytes(registryDTO);
        channel.writeAndFlush(body);
        logger.info("发送注册事件");
    }

    private RegistryDTO buildRegistryDTO(String brokerIp) {
        GlobalProperties globalProperties = CommonCache.getGlobalProperties();
        Integer brokerPort = globalProperties.getBrokerPort();
        String nameserverUser = globalProperties.getNameserverUser();
        String nameserverPassword = globalProperties.getNameserverPassword();

        RegistryDTO registryDTO = new RegistryDTO();
        registryDTO.setBrokerIp(brokerIp);
        registryDTO.setBrokerPort(brokerPort);
        registryDTO.setUser(nameserverUser);
        registryDTO.setPassword(nameserverPassword);
        return registryDTO;
    }
}

