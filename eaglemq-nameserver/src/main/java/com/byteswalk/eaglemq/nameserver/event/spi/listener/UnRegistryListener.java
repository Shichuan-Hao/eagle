package com.byteswalk.eaglemq.nameserver.event.spi.listener;

import cn.byteswalk.eaglemq.common.coder.TcpMsg;
import cn.byteswalk.eaglemq.common.enums.NameServerResponseCode;
import com.byteswalk.eaglemq.nameserver.common.CommonCache;
import com.byteswalk.eaglemq.nameserver.event.model.UnRegistryEvent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-12 17:52
 * @Description: UnRegistryListener
 * @Version: 1.0
 */
public class UnRegistryListener
        implements Listener<UnRegistryEvent> {

    @Override
    public void onReceive(UnRegistryEvent event) throws IllegalAccessException {
        ChannelHandlerContext channelHandlerContext = event.getChannelHandlerContext();
        Object reqId = channelHandlerContext.attr(AttributeKey.valueOf("reqId")).get();
        if (reqId == null) {
            channelHandlerContext.writeAndFlush(new TcpMsg(NameServerResponseCode.ERROR_USER_OR_PASSWORD.getCode(),
                    NameServerResponseCode.ERROR_USER_OR_PASSWORD.getDesc().getBytes()));
            channelHandlerContext.close();
            throw new IllegalAccessException("error account to connected!");
        }
        String reqIdStr = (String) reqId;
        CommonCache.getServiceInstanceManager().remove(reqIdStr);
    }
}
