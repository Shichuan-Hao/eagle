package cn.byteswalk.eaglemq.common.remote;

import cn.byteswalk.eaglemq.common.cache.NameServerSyncFutureManager;
import cn.byteswalk.eaglemq.common.coder.TcpMsg;
import cn.byteswalk.eaglemq.common.dto.*;
import cn.byteswalk.eaglemq.common.enums.NameServerResponseCode;
import com.alibaba.fastjson2.JSON;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-05 17:36
 * @Description: 处理注册中心 NameServer 返回给客户端的数据内容
 * @Version: 1.0
 */
@ChannelHandler.Sharable
public class NameServerRemoteRespHandler
        extends SimpleChannelInboundHandler {

    private final Logger logger = LoggerFactory.getLogger(NameServerRemoteRespHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) {
        TcpMsg tcpMsg = (TcpMsg) msg;
        int code = tcpMsg.getCode();
        byte[] body = tcpMsg.getBody();
        SyncFuture syncFuture = null;
        if (NameServerResponseCode.REGISTRY_SUCCESS.getCode() == code
                || NameServerResponseCode.ERROR_USER_OR_PASSWORD.getCode() == code) {
            syncFuture = prepareSyncFuture(body, ServiceRegistryRespDTO.class);
        } else if (NameServerResponseCode.HEART_BEAT_SUCCESS.getCode() == code) {
            syncFuture = prepareSyncFuture(body, PullBrokerIpRespDTO.class);
        } else if (NameServerResponseCode.QUERY_BROKER_DATA.getCode() == code) {
            syncFuture = prepareSyncFuture(body, EagleMqBrokerDataDTO.class);
        } else {
            logger.warn("Received unrecognized response code: {}", code);
            return;
        }
        buildSyncFutureResponse(syncFuture, tcpMsg);
    }

    private <T extends BaseRemoteDTO> SyncFuture prepareSyncFuture(byte[] body, Class<T> clazz) {
        T responseDTO = JSON.parseObject(body, clazz);
        // 通过反射获取DTO中的getMsgId方法
        String msgId = null;
        try {
            Method getMsgIdMethod = clazz.getMethod("getMsgId");
            msgId = (String) getMsgIdMethod.invoke(responseDTO);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return NameServerSyncFutureManager.get(msgId);
    }

    private void buildSyncFutureResponse(SyncFuture syncFuture, TcpMsg tcpMsg) {
        if (syncFuture != null) {
            syncFuture.setResponse(tcpMsg);
        }
    }


}

