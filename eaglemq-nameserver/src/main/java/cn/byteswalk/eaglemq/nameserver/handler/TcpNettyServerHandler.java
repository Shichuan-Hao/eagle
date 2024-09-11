package cn.byteswalk.eaglemq.nameserver.handler;

import cn.byteswalk.eaglemq.common.coder.TcpMsg;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-05 16:15
 * @Description:
 * @Version: 1.0
 */
@ChannelHandler.Sharable
public class TcpNettyServerHandler
        extends SimpleChannelInboundHandler {

//    private static final Logger log = LoggerFactory.getLogger(TcpNettyServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
//        log.info("Accept msg: {}", JSON.toJSONString(msg));
        // 解析成特定的事件，然后发送事件消息
        TcpMsg tcpMsg = (TcpMsg) msg;
        System.out.println(JSON.toJSONString(tcpMsg));
    }
}

