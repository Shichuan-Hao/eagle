package com.byteswalk.eaglemq.nameserver.event.spi.listener;

import cn.byteswalk.eaglemq.common.coder.TcpMsg;
import cn.byteswalk.eaglemq.common.enums.NameServerResponseCode;
import com.byteswalk.eaglemq.nameserver.common.CommonCache;
import com.alibaba.fastjson2.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import com.byteswalk.eaglemq.nameserver.event.model.HeartBeatEvent;
import com.byteswalk.eaglemq.nameserver.store.ServiceInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-12 17:50
 * @Description: 心跳事件监听器
 * @Version: 1.0
 */
public class HeartBeatListener
        implements Listener<HeartBeatEvent> {

    private final Logger logger = LoggerFactory.getLogger(HeartBeatListener.class);

    @Override
    public void onReceive(HeartBeatEvent event) throws IllegalAccessException {
        //把存在的实例保存下来
        ChannelHandlerContext channelHandlerContext = event.getChannelHandlerContext();
        //之前做过认证
        Object reqId = channelHandlerContext.attr(AttributeKey.valueOf("reqId")).get();
        if (reqId == null) {
            TcpMsg tcpMsg = new TcpMsg(NameServerResponseCode.ERROR_USER_OR_PASSWORD.getCode(),
                    NameServerResponseCode.ERROR_USER_OR_PASSWORD.getDesc().getBytes());
            channelHandlerContext.writeAndFlush(tcpMsg);
            channelHandlerContext.close();
            throw new IllegalAccessException("error account to connected!");
        }
        logger.info("接收到心跳数据：{}", event);
        //心跳，客户端每隔3秒请求一次
        String brokerIdentifyStr = (String) reqId;
        String[] brokerInfoArr = brokerIdentifyStr.split(":");
        long currentTimestamp = System.currentTimeMillis();
        // 保存注册到服务中心的服务实例信息
        ServiceInstance serviceInstance = new ServiceInstance();
        serviceInstance.setBrokerIp(brokerInfoArr[0]);
        serviceInstance.setBrokerPort(Integer.valueOf(brokerInfoArr[1]));
        serviceInstance.setLastHeartBeatTime(currentTimestamp);
        logger.info(JSON.toJSONString(serviceInstance));
        CommonCache.getServiceInstanceManager().putIfExist(serviceInstance);
    }
}
