package cn.byteswalk.eaglemq.common.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-05 13:10
 * @Description: 消息编码类
 * @Version: 1.0
 */
public class TcpMsgEncoder
        extends MessageToByteEncoder {


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object msg, ByteBuf out) throws Exception {
        TcpMsg tcpMsg = (TcpMsg) msg;
        out.writeShort(tcpMsg.getMagic());
        out.writeInt(tcpMsg.getCode());
        out.writeInt(tcpMsg.getLen());
        out.writeBytes(tcpMsg.getBody());
    }
}

