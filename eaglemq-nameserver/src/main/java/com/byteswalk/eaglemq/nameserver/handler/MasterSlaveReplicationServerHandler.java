package com.byteswalk.eaglemq.nameserver.handler;

import cn.byteswalk.eaglemq.common.coder.TcpMsg;
import cn.byteswalk.eaglemq.common.enums.NameServerEventCode;
import com.alibaba.fastjson.JSON;
import com.byteswalk.eaglemq.nameserver.event.EventBus;
import com.byteswalk.eaglemq.nameserver.event.model.Event;
import com.byteswalk.eaglemq.nameserver.event.model.StartReplicationEvent;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-10-09 17:01
 * @Description:
 * @Version: 1.0
 */
@ChannelHandler.Sharable
public class MasterSlaveReplicationServerHandler extends SimpleChannelInboundHandler<Object> {

    private EventBus eventBus;

    public MasterSlaveReplicationServerHandler(EventBus eventBus) {
        this.eventBus = eventBus;
        this.eventBus.init();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        TcpMsg tcpMsg = (TcpMsg) msg;
        int code = tcpMsg.getCode();
        byte[] body = tcpMsg.getBody();
        //从节点发起链接，在master端通过密码验证，建立链接
        Event event = null;
        if (NameServerEventCode.START_REPLICATION.getCode() == code) {
            event = JSON.parseObject(body, StartReplicationEvent.class);
        }
        //channelHandlerContext -》 map
        //链接建立完成后，master收到的数据，同步发送给slave节点
        //channelHandlerContext.writeAndFlush();
        assert event != null;
        event.setChannelHandlerContext(channelHandlerContext);
        eventBus.publish(event);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}

