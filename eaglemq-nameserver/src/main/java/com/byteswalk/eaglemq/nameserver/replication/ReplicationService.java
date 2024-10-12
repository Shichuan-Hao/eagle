package com.byteswalk.eaglemq.nameserver.replication;

import cn.byteswalk.eaglemq.common.coder.TcpMsgDecoder;
import cn.byteswalk.eaglemq.common.coder.TcpMsgEncoder;
import cn.byteswalk.eaglemq.common.utils.AssertUtils;
import com.byteswalk.eaglemq.nameserver.common.CommonCache;
import com.byteswalk.eaglemq.nameserver.common.MasterSlaveReplicationProperties;
import com.byteswalk.eaglemq.nameserver.common.NameserverProperties;
import com.byteswalk.eaglemq.nameserver.common.TraceReplicationProperties;
import com.byteswalk.eaglemq.nameserver.enums.ReplicationModeEnum;
import com.byteswalk.eaglemq.nameserver.event.EventBus;
import com.byteswalk.eaglemq.nameserver.handler.MasterSlaveReplicationServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-10-09 15:30
 * @Description: 集群复制服务
 * @Version: 1.0
 */
public class ReplicationService {

    private final Logger logger = LoggerFactory.getLogger(ReplicationService.class);

    // 参数的校验
    // 根据参数判断复制的方式
    // 启动netty进程/不启动（单机版本）

    public void checkProperties() {
        NameserverProperties nameserverProperties = CommonCache.getNameserverProperties();
        String mode = nameserverProperties.getReplicationMode();
        if (StringUtil.isNullOrEmpty(mode)) {
            logger.info("执行单机模式");
        }
        // 为空，参数不合法，抛出异常
        ReplicationModeEnum replicationModeEnum = ReplicationModeEnum.of(mode);
        AssertUtils.isNotNull(replicationModeEnum, "复制模式参数异常");
        if (replicationModeEnum == ReplicationModeEnum.TRACE) {
            // 链路复制
            TraceReplicationProperties traceReplicationProperties = nameserverProperties.getTraceReplicationProperties();
        } else {
            // 主从复制
            MasterSlaveReplicationProperties masterSlaveReplicationProperties = nameserverProperties.getMasterSlaveReplicationProperties();
            AssertUtils.isNotBlank(masterSlaveReplicationProperties.getMaster(), "master参数不能为空");
            AssertUtils.isNotBlank(masterSlaveReplicationProperties.getRole(), "role参数不能为空");
            AssertUtils.isNotBlank(masterSlaveReplicationProperties.getType(), "type参数不能为空");
            AssertUtils.isNotNull(masterSlaveReplicationProperties.getPort(), "同步端口不能为空");
        }
    }

    public void startReplicationTask(ReplicationModeEnum replicationModeEnum) {
        if (replicationModeEnum == null) {
            // 单机版本，不用处理
            return;
        }
        int port = 0;
        if (replicationModeEnum == ReplicationModeEnum.MASTER_SLAVE) {
            port = CommonCache.getNameserverProperties().getMasterSlaveReplicationProperties().getPort();
        }
        int replicationPort = port;
        Thread replicationTask = new Thread(() -> {
            // 处理网络io中的accept事件
            NioEventLoopGroup bossGroup = new NioEventLoopGroup();
            // 处理网络io中的read&write事件
            NioEventLoopGroup workerGroup = new NioEventLoopGroup();
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childHandler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel ch) throws Exception {
                    ch.pipeline().addLast(new TcpMsgDecoder());
                    ch.pipeline().addLast(new TcpMsgEncoder());
                    if (replicationModeEnum == ReplicationModeEnum.MASTER_SLAVE) {
                        // 主从复制
                        ch.pipeline().addLast(new MasterSlaveReplicationServerHandler(new EventBus("replication-task-")));
                    } else if (replicationModeEnum == ReplicationModeEnum.TRACE) {
                        // 链路复制
                        ch.pipeline().addLast(null);
                    }
                }
            });
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
                logger.info("nameserver's replication application is closed");
            }));
            ChannelFuture channelFuture = null;
            try {
                channelFuture = bootstrap.bind(replicationPort).sync();
                logger.info("start nameserver's replication application on port: {}", replicationPort);
                // 阻塞代码
                channelFuture.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        replicationTask.setName("replication-task-");
        replicationTask.start();
    }

}

