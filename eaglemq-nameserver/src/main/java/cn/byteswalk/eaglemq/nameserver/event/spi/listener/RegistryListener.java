package cn.byteswalk.eaglemq.nameserver.event.spi.listener;

import cn.byteswalk.eaglemq.common.coder.TcpMsg;
import cn.byteswalk.eaglemq.common.constants.NameServerConstants;
import cn.byteswalk.eaglemq.common.enums.NameServerRespCode;
import cn.byteswalk.eaglemq.nameserver.commom.CommonCache;
import cn.byteswalk.eaglemq.nameserver.event.model.RegistryEvent;
import cn.byteswalk.eaglemq.nameserver.store.ServiceInstance;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

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
    public void onReceive(RegistryEvent event)
            throws IllegalAccessException {
        // 安全认证，密码校验，可以扩展
        String user          = event.getUser();
        String password      = event.getPassword();
        String rightUser     = CommonCache.getPropertiesLoader().getProperties("nameserver.user");
        String rightPassword = CommonCache.getPropertiesLoader().getProperties("nameserver.properties");

        Objects.requireNonNull(user, "user is null!");
        Objects.requireNonNull(password, "password is null!");
        Objects.requireNonNull(rightUser, "the user read from property file is null!");
        Objects.requireNonNull(rightPassword, "the password read from property file is null");

        ChannelHandlerContext channelHandlerContext = event.getChannelHandlerContext();
        if (!rightUser.equals(user) && !rightPassword.equals(password)) {

            TcpMsg tcpMsg = new TcpMsg(NameServerRespCode.ERROR_USER_OR_PASSWORD.getCode(),
                    NameServerRespCode.ERROR_USER_OR_PASSWORD.getDesc().getBytes(StandardCharsets.UTF_8));
            // 通知调用方
            channelHandlerContext.writeAndFlush(tcpMsg);
            // 关闭 channel，使调用发不要发请求了
            channelHandlerContext.close();
            throw new IllegalAccessException("error account to connected, user and password do not match, please try again!");
        }
        // 返回一个唯一标识：req-id，代表已经通过安全验证
        String reqId = event.getBrokerIp() + NameServerConstants.SPLIT_REQ_ID + event.getBrokerPort();
        channelHandlerContext.attr(AttributeKey.valueOf("req-id")).set(reqId);

        ServiceInstance serviceInstance = new ServiceInstance();
        serviceInstance.setBrokerIp(event.getBrokerIp());
        serviceInstance.setBrokerPort(event.getBrokerPort());
        serviceInstance.setFirstRegistryTime(System.currentTimeMillis());
        // 放入内存之中
        CommonCache.getServiceInstanceManager().put(serviceInstance);
        // 注册事件的响应
        TcpMsg tcpMsg = new TcpMsg(NameServerRespCode.REGISTRY_SUCCESS.getCode(),
                NameServerRespCode.REGISTRY_SUCCESS.getDesc().getBytes(StandardCharsets.UTF_8));
        channelHandlerContext.writeAndFlush(tcpMsg);
    }

}

