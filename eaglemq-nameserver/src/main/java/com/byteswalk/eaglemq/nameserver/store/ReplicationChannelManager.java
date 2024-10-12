package com.byteswalk.eaglemq.nameserver.store;

import com.google.common.collect.Maps;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-10-09 17:11
 * @Description:
 * @Version: 1.0
 */
public class ReplicationChannelManager {

    private static Map<String, ChannelHandlerContext> channelHandlerContextMap = Maps.newConcurrentMap();

    public void put(String reqId, ChannelHandlerContext channelHandlerContext) {
        channelHandlerContextMap.put(reqId, channelHandlerContext);
    }

    public void get(String reqId) {
        channelHandlerContextMap.get(reqId);
    }

}

