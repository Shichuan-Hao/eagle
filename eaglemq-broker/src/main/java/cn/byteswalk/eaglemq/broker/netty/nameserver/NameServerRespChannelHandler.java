package cn.byteswalk.eaglemq.broker.netty.nameserver;

import cn.byteswalk.eaglemq.common.coder.TcpMsg;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-18 10:21
 * @Description: 注册中心 Nameserver 通道
 * @Version: 1.0
 */
@ChannelHandler.Sharable
public class NameServerRespChannelHandler extends SimpleChannelInboundHandler<TcpMsg> {


    private final Logger logger = LoggerFactory.getLogger(NameServerRespChannelHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TcpMsg tcpMsg)
            throws Exception {
        logger.info("resp: {}", JSON.toJSONString(tcpMsg));
    }
}

