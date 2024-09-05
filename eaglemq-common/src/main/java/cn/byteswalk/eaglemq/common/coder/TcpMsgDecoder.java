package cn.byteswalk.eaglemq.common.coder;

import cn.byteswalk.eaglemq.common.constants.CommonConstants;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-05 13:09
 * @Description: 消息解码类
 * @Version: 1.0
 */
public class TcpMsgDecoder
        extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list)
            throws Exception {
        if (byteBuf.readableBytes() > 2 + 4 + 4) {
            if (byteBuf.readShort() != CommonConstants.DEFAULT_MAGIC_NUM) {
                ctx.close();
                return;
            }
            int code = byteBuf.readInt();
            int len = byteBuf.readInt();
            if (byteBuf.readableBytes() < len) {
                ctx.close();
                return;
            }
            byte[] body = new byte[len];
            byteBuf.readBytes(body);
            TcpMsg tcpMsg = new TcpMsg();
            tcpMsg.setMagic(CommonConstants.DEFAULT_MAGIC_NUM);
            tcpMsg.setCode(code);
            tcpMsg.setLen(len);
            tcpMsg.setBody(body);
            list.add(tcpMsg);
        }
    }
}

