package cn.byteswalk.eaglemq.common.remote;

import cn.byteswalk.eaglemq.common.coder.TcpMsgDecoder;
import cn.byteswalk.eaglemq.common.coder.TcpMsgEncoder;
import cn.byteswalk.eaglemq.common.constants.TcpConstants;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-05 17:28
 * @Description: 对注册中心 NameServer 进行远程访问的一个客户端工具
 * @Version: 1.0
 */
public class NameServerNettyRemoteClient {

    private String ip;
    private Integer port;

    public NameServerNettyRemoteClient(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }

    private EventLoopGroup clientGroup = new NioEventLoopGroup();
    private Bootstrap bootstrap;
    private Channel channel;

    /**
     * 远程连接的初始化
     */
    public void  buildConnection() {
        bootstrap.group(clientGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ByteBuf delimiter = Unpooled.copiedBuffer(TcpConstants.DEFAULT_DECODE_CHAR.getBytes());
                channel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024*8, delimiter));
                channel.pipeline().addLast(new TcpMsgDecoder());
                channel.pipeline().addLast(new TcpMsgEncoder());
//                channel.pipeline().addLast(new NameServerNettyRemoteClient());
            }
        });

    }

}

