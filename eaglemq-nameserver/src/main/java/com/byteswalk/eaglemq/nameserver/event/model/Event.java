package com.byteswalk.eaglemq.nameserver.event.model;

import io.netty.channel.ChannelHandlerContext;

/**
 * @Author idea
 * @Date: Created in 14:16 2024/5/4
 * @Description
 */
public abstract class Event {

    private long timeStamp;
    //?
    private ChannelHandlerContext channelHandlerContext;

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public ChannelHandlerContext getChannelHandlerContext() {
        return channelHandlerContext;
    }

    public void setChannelHandlerContext(ChannelHandlerContext channelHandlerContext) {
        this.channelHandlerContext = channelHandlerContext;
    }

}
