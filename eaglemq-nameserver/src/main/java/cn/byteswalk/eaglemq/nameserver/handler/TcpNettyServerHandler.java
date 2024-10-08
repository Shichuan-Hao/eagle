package cn.byteswalk.eaglemq.nameserver.handler;

import cn.byteswalk.eaglemq.common.coder.TcpMsg;
import cn.byteswalk.eaglemq.common.enums.NameServerEventCode;
import cn.byteswalk.eaglemq.nameserver.event.model.Event;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import cn.byteswalk.eaglemq.nameserver.event.EventBus;
import cn.byteswalk.eaglemq.nameserver.event.model.HeartBeatEvent;
import cn.byteswalk.eaglemq.nameserver.event.model.RegistryEvent;
import cn.byteswalk.eaglemq.nameserver.event.model.UnRegistryEvent;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-05 16:15
 * @Description: Tcp Netty 处理器，主要接收Netty的网络请求，而后解析
 * @Version: 1.0
 */
@ChannelHandler.Sharable
public class TcpNettyServerHandler extends SimpleChannelInboundHandler {

    private EventBus eventBus;

    public TcpNettyServerHandler(EventBus eventBus) {
        this.eventBus = eventBus;
        this.eventBus.init();
    }

    //1.网络请求的接收(netty完成)
    //2.事件发布器的实现（EventBus-》event）Spring的事件，Google Guaua
    //3.事件处理器的实现（Listener-》处理event）
    //4.数据存储（基于Map本地内存的方式存储）
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        TcpMsg tcpMsg = (TcpMsg) msg;
        int code = tcpMsg.getCode();
        byte[] body = tcpMsg.getBody();
        Event event = null;
        if (NameServerEventCode.REGISTRY.getCode() == code) {
            event = JSON.parseObject(body, RegistryEvent.class);
        } else if (NameServerEventCode.HEART_BEAT.getCode() == code) {
            event = new HeartBeatEvent();
        }
        assert event != null;
        event.setChannelHandlerContext(channelHandlerContext);
        eventBus.publish(event);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //如果依赖任务剔除节点，会有三个心跳周期的延迟，做到链接断开立马剔除的效果
        UnRegistryEvent unRegistryEvent = new UnRegistryEvent();
        unRegistryEvent.setChannelHandlerContext(ctx);
        eventBus.publish(unRegistryEvent);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
