package cn.byteswalk.eaglemq.nameserver.event.spi.listener;

import cn.byteswalk.eaglemq.common.coder.TcpMsg;
import cn.byteswalk.eaglemq.common.enums.NameServerRespCode;
import cn.byteswalk.eaglemq.nameserver.CommonCache;
import cn.byteswalk.eaglemq.nameserver.event.model.HeartBeatEvent;
import cn.byteswalk.eaglemq.nameserver.store.ServiceInstance;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

import java.nio.charset.StandardCharsets;


/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-12 17:50
 * @Description: 心跳
 * @Version: 1.0
 */
public class HeartBeatListener
        implements Listener<HeartBeatEvent> {

    @Override
    public void onReceive(HeartBeatEvent event)
            throws Exception {
        // 认证通过之后，心跳包，将实例保存下来
        ChannelHandlerContext channelHandlerContext = event.getChannelHandlerContext();
        // 如果不为空，代表已经通过认证，这里可以扩展 TODO
        if (channelHandlerContext.attr(AttributeKey.valueOf("rep-id")).get() == null) {
            TcpMsg tcpMsg = new TcpMsg(NameServerRespCode.ERROR_USER_OR_PASSWORD.getCode(),
                    NameServerRespCode.ERROR_USER_OR_PASSWORD.getDesc().getBytes(StandardCharsets.UTF_8));
            // 通知调用方
            channelHandlerContext.writeAndFlush(tcpMsg);
            // 关闭 channel，使调用发不要发请求了
            channelHandlerContext.close();
            throw new IllegalAccessException(NameServerRespCode.NOT_AUTHENTICATED.getDesc());
        }
        ServiceInstance serviceInstance = new ServiceInstance();
        serviceInstance.setBrokerIp(event.getBrokerIp());
        serviceInstance.setBrokerPort(event.getBrokerPort());
        serviceInstance.setLastHeartBeatTime(System.currentTimeMillis());
        CommonCache.getServiceInstanceManager().putIfExist(serviceInstance);
    }
}

