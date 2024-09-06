package cn.byteswalk.eaglemq.common.remote;

import cn.byteswalk.eaglemq.common.cache.NameServerSyncFutureManager;
import cn.byteswalk.eaglemq.common.coder.TcpMsg;
import cn.byteswalk.eaglemq.common.coder.TcpMsgDecoder;
import cn.byteswalk.eaglemq.common.coder.TcpMsgEncoder;
import cn.byteswalk.eaglemq.common.constants.TcpConstants;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

import java.util.concurrent.ExecutionException;

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
                channel.pipeline().addLast(new NameServerRemoteRespHandler());
            }
        });

        ChannelFuture channelFuture = null;
        try {
            channelFuture = bootstrap
                    .connect(ip, port)
                    .sync()
                    .addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (!channelFuture.isSuccess()) {
                        throw new RuntimeException("connecting nameserver has error");
                    }
                }
            });
            // 初始化建立长连接
            channel = channelFuture.channel();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public TcpMsg sendSyncMsg(TcpMsg tcpMsg, String msgId) {
        // future 设计机制 jdk8里面有很多种，future，futuretask，CompletableFuture
        channel.writeAndFlush(tcpMsg);
        SyncFuture syncFuture = new SyncFuture();
        syncFuture.setMsgId(msgId);
        NameServerSyncFutureManager.put(msgId, syncFuture);
        try {
            return (TcpMsg)syncFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendAsyncMsg(TcpMsg tcpMsg) {
        channel.writeAndFlush(tcpMsg);
    }

}

