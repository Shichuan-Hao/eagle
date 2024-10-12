package com.byteswalk.eaglemq.broker.netty.nameserver;

import com.byteswalk.eaglemq.broker.cache.CommonCache;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import cn.byteswalk.eaglemq.common.coder.TcpMsg;
import cn.byteswalk.eaglemq.common.enums.NameServerResponseCode;

/**
 * @author erichlin
 * @create 2024/1/21 15:52
 * @description nameserver通道
 */
@ChannelHandler.Sharable
public class NameServerRespChannelHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        TcpMsg tcpMsg = (TcpMsg) msg;
        if(NameServerResponseCode.REGISTRY_SUCCESS.getCode() == tcpMsg.getCode()) {
            //注册成功！
            //开启一个定时任务，上报心跳数据给到nameserver
            CommonCache.getHeartBeatTaskManager().startTask();
        } else if (NameServerResponseCode.ERROR_USER_OR_PASSWORD.getCode() == tcpMsg.getCode()) {
            //验证失败，抛出异常
            throw new RuntimeException("error nameserver user or password");
        }
    }
}
