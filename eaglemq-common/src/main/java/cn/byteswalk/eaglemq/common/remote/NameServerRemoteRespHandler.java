package cn.byteswalk.eaglemq.common.remote;

import cn.byteswalk.eaglemq.common.cache.NameServerSyncFutureManager;
import cn.byteswalk.eaglemq.common.coder.TcpMsg;
import cn.byteswalk.eaglemq.common.dto.EagleMqBrokerDataDTO;
import cn.byteswalk.eaglemq.common.dto.PullBrokerIpRespDTO;
import cn.byteswalk.eaglemq.common.dto.ServiceRegistryRespDTO;
import cn.byteswalk.eaglemq.common.enums.NameServerResponseCode;
import com.alibaba.fastjson2.JSON;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-05 17:36
 * @Description: 处理注册中心 NameServer 返回给客户端的数据内容
 * @Version: 1.0
 */
@ChannelHandler.Sharable
public class NameServerRemoteRespHandler
        extends SimpleChannelInboundHandler {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) {
        TcpMsg tcpMsg = (TcpMsg) msg;
        int code = tcpMsg.getCode();
        byte[] body = tcpMsg.getBody();
        if (NameServerResponseCode.REGISTRY_SUCCESS.getCode() == code) {
            // msgId
            ServiceRegistryRespDTO serviceRegistryRespDTO = JSON.parseObject(body, ServiceRegistryRespDTO.class);
            SyncFuture syncFuture = NameServerSyncFutureManager.get(serviceRegistryRespDTO.getMsgId());
            buildSyncFutureResponse(syncFuture, tcpMsg);
        } else if (NameServerResponseCode.ERROR_USER_OR_PASSWORD.getCode() == code) {
            ServiceRegistryRespDTO serviceRegistryRespDTO = JSON.parseObject(body, ServiceRegistryRespDTO.class);
            SyncFuture syncFuture = NameServerSyncFutureManager.get(serviceRegistryRespDTO.getMsgId());
            buildSyncFutureResponse(syncFuture, tcpMsg);
        } else if (NameServerResponseCode.HEART_BEAT_SUCCESS.getCode() == code) {
            PullBrokerIpRespDTO pullBrokerIpRespDTO = JSON.parseObject(body, PullBrokerIpRespDTO.class);
            SyncFuture syncFuture = NameServerSyncFutureManager.get(pullBrokerIpRespDTO.getMsgId());
            buildSyncFutureResponse(syncFuture, tcpMsg);
        } else if (NameServerResponseCode.QUERY_BROKER_DATA.getCode() == code) {
            EagleMqBrokerDataDTO eagleMqBrokerDataDTO = JSON.parseObject(tcpMsg.getBody(), EagleMqBrokerDataDTO.class);
            SyncFuture syncFuture = NameServerSyncFutureManager.get(eagleMqBrokerDataDTO.getMsgId());
            buildSyncFutureResponse(syncFuture, tcpMsg);
        }
    }

    private void buildSyncFutureResponse(SyncFuture syncFuture, TcpMsg tcpMsg) {
        if (syncFuture != null) {
            syncFuture.setResponse(tcpMsg);
        }
    }
}

