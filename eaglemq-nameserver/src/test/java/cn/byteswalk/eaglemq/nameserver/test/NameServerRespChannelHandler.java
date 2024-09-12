package cn.byteswalk.eaglemq.nameserver.test;

import cn.byteswalk.eaglemq.common.coder.TcpMsg;
import com.alibaba.fastjson2.JSON;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler.Sharable
public class NameServerRespChannelHandler
        extends SimpleChannelInboundHandler {

    private final Logger logger = LoggerFactory.getLogger(NameServerRespChannelHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        TcpMsg tcpMsg = (TcpMsg) o;
        logger.info("resp:" + JSON.toJSONString(tcpMsg));
    }
}
