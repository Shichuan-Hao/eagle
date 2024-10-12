package com.byteswalk.eaglemq.nameserver.event.spi.listener;

import cn.byteswalk.eaglemq.common.coder.TcpMsg;
import cn.byteswalk.eaglemq.common.enums.NameServerResponseCode;
import com.byteswalk.eaglemq.nameserver.common.CommonCache;
import com.byteswalk.eaglemq.nameserver.event.model.StartReplicationEvent;
import com.byteswalk.eaglemq.nameserver.utils.NameserverUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-10-09 17:06
 * @Description: 开启同步复制监听器
 * @Version: 1.0
 */
public class StartReplicationListener implements Listener<StartReplicationEvent> {


    @Override
    public void onReceive(StartReplicationEvent event) throws Exception {
        boolean isVerify = NameserverUtils.isVerify(event.getUser(), event.getPassword());
        ChannelHandlerContext channelHandlerContext = event.getChannelHandlerContext();
        if (!isVerify) {
            TcpMsg tcpMsg = new TcpMsg(NameServerResponseCode.ERROR_USER_OR_PASSWORD.getCode(),
                    NameServerResponseCode.ERROR_USER_OR_PASSWORD.getDesc().getBytes());
            channelHandlerContext.writeAndFlush(tcpMsg);
            channelHandlerContext.close();
            throw new IllegalAccessException("error account to connected!");
        }
        String reqId = event.getSlaveIp() + ":" + event.getSlavePort();
        channelHandlerContext.attr(AttributeKey.valueOf("reqId")).set(reqId);
        CommonCache.getReplicationChannelManager().put(reqId, channelHandlerContext);
    }
}

