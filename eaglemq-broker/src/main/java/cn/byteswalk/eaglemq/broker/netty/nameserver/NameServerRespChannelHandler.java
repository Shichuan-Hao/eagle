package cn.byteswalk.eaglemq.broker.netty.nameserver;

import cn.byteswalk.eaglemq.broker.cache.CommonCache;
import cn.byteswalk.eaglemq.common.coder.TcpMsg;
import cn.byteswalk.eaglemq.common.enums.NameServerRespCode;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-18 10:21
 * @Description: 注册中心 Nameserver 通道
 * @Version: 1.0
 */
@ChannelHandler.Sharable
public class NameServerRespChannelHandler extends SimpleChannelInboundHandler<TcpMsg> {


    private final Logger logger = LoggerFactory.getLogger(NameServerRespChannelHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TcpMsg tcpMsg)
            throws Exception {
        logger.info("Broker registration nameserver response results: {}", JSON.toJSONString(tcpMsg));
        // 注册成功
        if (NameServerRespCode.REGISTRY_SUCCESS.getCode() == tcpMsg.getCode()) {
            // 开启一个定时任务，上报心跳数据给到nameserver，需要做幂等
            CommonCache.getHeartBeatTaskManager().startTask();
        } else if (NameServerRespCode.ERROR_USER_OR_PASSWORD.getCode() == tcpMsg.getCode()) {
            // 注册失败，抛出异常
            throw new RuntimeException("error nameserver user or password");
        }
    }
}

