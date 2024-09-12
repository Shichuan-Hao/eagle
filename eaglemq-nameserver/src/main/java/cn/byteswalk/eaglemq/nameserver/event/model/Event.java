package cn.byteswalk.eaglemq.nameserver.event.model;

import io.netty.channel.ChannelHandlerContext;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-12 11:33
 * @Description: 事件总类
 * @Version: 1.0
 */
public abstract class Event {

    private long timestamp;

    /**
     * 主要是系统构建
     */
    private ChannelHandlerContext channelHandlerContext;
}

