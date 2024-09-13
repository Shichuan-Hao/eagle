package cn.byteswalk.eaglemq.nameserver.event.model;

import io.netty.channel.ChannelHandlerContext;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-12 11:33
 * @Description: 事件总类
 * @Version: 1.0
 */
public abstract class Event {

    private String brokerIp;
    private Integer brokerPort;
    private long timestamp;

    /**
     * 主要是系统构建
     */
    private ChannelHandlerContext channelHandlerContext;


    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public ChannelHandlerContext getChannelHandlerContext() {
        return channelHandlerContext;
    }

    public void setChannelHandlerContext(ChannelHandlerContext channelHandlerContext) {
        this.channelHandlerContext = channelHandlerContext;
    }

    public String getBrokerIp() {
        return brokerIp;
    }

    public void setBrokerIp(String brokerIp) {
        this.brokerIp = brokerIp;
    }

    public Integer getBrokerPort() {
        return brokerPort;
    }

    public void setBrokerPort(Integer brokerPort) {
        this.brokerPort = brokerPort;
    }
}

