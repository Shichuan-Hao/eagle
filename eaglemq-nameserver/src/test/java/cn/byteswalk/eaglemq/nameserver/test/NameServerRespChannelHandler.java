package cn.byteswalk.eaglemq.nameserver.test;

import cn.byteswalk.eaglemq.common.coder.TcpMsg;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-10 17:11
 * @Description: NameServerRespChannelHandler
 * @Version: 1.0
 */
@ChannelHandler.Sharable
public class NameServerRespChannelHandler
        extends SimpleChannelInboundHandler<Object> {


//    private static final Logger log = LoggerFactory.getLogger(TcpNettyServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
//        log.info("\33[0;4mAccept msg: {}\33[0;4m", JSON.toJSONString(msg));
        TcpMsg tcpMsg = (TcpMsg) msg;
        System.out.println(JSON.toJSONString(tcpMsg));
    }

}

