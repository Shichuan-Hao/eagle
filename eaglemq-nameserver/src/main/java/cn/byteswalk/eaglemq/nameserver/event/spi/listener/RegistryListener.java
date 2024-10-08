package cn.byteswalk.eaglemq.nameserver.event.spi.listener;

import cn.byteswalk.eaglemq.common.coder.TcpMsg;
import cn.byteswalk.eaglemq.common.enums.NameServerResponseCode;
import cn.byteswalk.eaglemq.nameserver.common.CommonCache;
import cn.byteswalk.eaglemq.nameserver.event.model.RegistryEvent;
import cn.byteswalk.eaglemq.nameserver.store.ServiceInstance;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-12 17:51
 * @Description: RegistryListener
 * @Version: 1.0
 */
public class RegistryListener
        implements Listener<RegistryEvent> {

    private static final Logger logger = LoggerFactory.getLogger(RegistryListener.class);

    @Override
    public void onReceive(RegistryEvent event) throws IllegalAccessException {
        //安全认证
        String rightUser = CommonCache.getPropertiesLoader().getProperty("nameserver.user");
        String rightPassword = CommonCache.getPropertiesLoader().getProperty("nameserver.password");
        ChannelHandlerContext channelHandlerContext = event.getChannelHandlerContext();
        if (!rightUser.equals(event.getUser()) || !rightPassword.equals(event.getPassword())) {
            TcpMsg tcpMsg = new TcpMsg(NameServerResponseCode.ERROR_USER_OR_PASSWORD.getCode(),
                    NameServerResponseCode.ERROR_USER_OR_PASSWORD.getDesc().getBytes());
            channelHandlerContext.writeAndFlush(tcpMsg);
            channelHandlerContext.close();
            throw new IllegalAccessException("error account to connected!");
        }
        logger.info("注册事件接收数据：{}", event);
        channelHandlerContext.attr(AttributeKey.valueOf("reqId")).set(event.getBrokerIp() + ":" + event.getBrokerPort());
        ServiceInstance serviceInstance = new ServiceInstance();
        serviceInstance.setBrokerIp(event.getBrokerIp());
        serviceInstance.setBrokerPort(event.getBrokerPort());
        serviceInstance.setFirstRegistryTime(System.currentTimeMillis());
        CommonCache.getServiceInstanceManager().put(serviceInstance);
        TcpMsg tcpMsg = new TcpMsg(NameServerResponseCode.REGISTRY_SUCCESS.getCode(),
                NameServerResponseCode.REGISTRY_SUCCESS.getDesc().getBytes());
        channelHandlerContext.writeAndFlush(tcpMsg);
    }

}
