package cn.byteswalk.eaglemq.nameserver;


import cn.byteswalk.eaglemq.common.coder.TcpMsg;
import cn.byteswalk.eaglemq.common.coder.TcpMsgDecoder;
import cn.byteswalk.eaglemq.common.coder.TcpMsgEncoder;
import cn.byteswalk.eaglemq.nameserver.test.NameServerRespChannelHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class TestNameServerSuite {


    private final Logger logger = LoggerFactory.getLogger(TestNameServerSuite.class);

    private EventLoopGroup clientGroup = new NioEventLoopGroup();
    private Bootstrap bootstrap = new Bootstrap();
    private Channel channel;
    private String DEFAULT_NAMESERVER_IP = "127.0.0.1";

    @Before
    public void setUp() {
        bootstrap.group(clientGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new TcpMsgDecoder());
                socketChannel.pipeline().addLast(new TcpMsgEncoder());
                socketChannel.pipeline().addLast(new NameServerRespChannelHandler());
            }
        });
        ChannelFuture channelFuture = null;
        try {
            channelFuture = bootstrap.connect(DEFAULT_NAMESERVER_IP, 9090).sync();
            channel = channelFuture.channel();
            logger.info("success connected to nameserver!");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSendMsg() {
        for (int i = 0; i < 100; i++) {
            try {
                System.out.println("isActive:" + channel.isActive());
                TimeUnit.SECONDS.sleep(1);
                String msgBody = "this is client test string";
                TcpMsg tcpMsg = new TcpMsg(1, msgBody.getBytes());
                channel.writeAndFlush(tcpMsg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
