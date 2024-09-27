package cn.byteswalk.eaglemq.nameserver.handler;

import cn.byteswalk.eaglemq.common.coder.TcpMsg;
import cn.byteswalk.eaglemq.common.enums.NameServerEventCode;
import cn.byteswalk.eaglemq.nameserver.event.EventBus;
import cn.byteswalk.eaglemq.nameserver.event.model.Event;
import cn.byteswalk.eaglemq.nameserver.event.model.HeartBeatEvent;
import cn.byteswalk.eaglemq.nameserver.event.model.RegistryEvent;
import cn.byteswalk.eaglemq.nameserver.event.model.UnRegistryEvent;


import com.alibaba.fastjson2.JSON;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Objects;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-05 16:15
 * @Description: Tcp Netty 处理器，主要接收Netty的网络请求，而后解析
 * @Version: 1.0
 */
@ChannelHandler.Sharable
public class TcpNettyServerHandler
        extends SimpleChannelInboundHandler<Object> {

    private static final Logger log = LoggerFactory.getLogger(TcpNettyServerHandler.class);

    private EventBus eventBus;

    public TcpNettyServerHandler(EventBus eventBus) {
        this.eventBus = eventBus;
        this.eventBus.init();
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg)
            throws Exception{
        log.info("TcpNettyServerHandler-channelRead0 received data:{}", JSON.toJSONString(msg));
        log.info("Accept msg: {}", JSON.toJSONString(msg));
        // 解析成特定的事件，然后发送事件消息
        TcpMsg tcpMsg = (TcpMsg) msg;
        int code = tcpMsg.getCode();
        byte[] body = tcpMsg.getBody();
        NameServerEventCode eventCode = NameServerEventCode.fromCode(code);
        Event event = null;
        switch (eventCode) {
            case REGISTRY:
                event = JSON.parseObject(body, RegistryEvent.class);
                break;
            case UN_REGISTRY:
                event = JSON.parseObject(body, UnRegistryEvent.class);
                break;
            case HEART_BEAT:
//                event = JSON.parseObject(body, HeartBeatEvent.class);
                event = new HeartBeatEvent();
                break;
            default:
                throw new IllegalArgumentException("unknown code!");
        }
        event.setChannelHandlerContext(ctx);

        eventBus.publish(event);
    }
}

