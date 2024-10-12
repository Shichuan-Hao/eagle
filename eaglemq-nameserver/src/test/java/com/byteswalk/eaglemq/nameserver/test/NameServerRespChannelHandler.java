package com.byteswalk.eaglemq.nameserver.test;

import cn.byteswalk.eaglemq.common.coder.TcpMsg;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

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
//        System.out.println("resp:" + JSON.toJSONString(tcpMsg));
    }
}
