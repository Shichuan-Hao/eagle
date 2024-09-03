package cn.byteswalk.eaglemqconsole.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.util.UUID;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-03 18:05
 * @Description:
 * @Version: 1.0
 */
public class NettyRemoteConfig {

    @Value("${mq.nameserver.ip:127.0.0.1}")
    private String nameServerIp;
    @Value("${mq.nameserver.port:9093}")
    private Integer nameServerPort;
    @Value("${mq.nameserver.pwd:eagle_mq}")
    private String nameServerPwd;
    @Value("${mq.nameserver.user:eagle_mq}")
    private String nameServerUser;

//    @Bean
//    public NameServerNettyRemoteClient nameServerNettyRemoteClient() {
//        NameServerNettyRemoteClient nameServerNettyRemoteClient = new NameServerNettyRemoteClient(nameServerIp,nameServerPort);
//        nameServerNettyRemoteClient.buildConnection();
//        String registryMsgId = UUID.randomUUID().toString();
//        ServiceRegistryReqDTO serviceRegistryReqDTO = new ServiceRegistryReqDTO();
//        serviceRegistryReqDTO.setMsgId(registryMsgId);
//        serviceRegistryReqDTO.setUser(this.nameServerUser);
//        serviceRegistryReqDTO.setPassword(this.nameServerPwd);
//        serviceRegistryReqDTO.setRegistryType(RegistryTypeEnum.CONSUMER.getCode());
//        TcpMsg tcpMsg = new TcpMsg(NameServerEventCode.REGISTRY.getCode(), JSON.toJSONBytes(serviceRegistryReqDTO));
//        TcpMsg registryResponse = nameServerNettyRemoteClient.sendSyncMsg(tcpMsg, registryMsgId);
//        if (NameServerResponseCode.REGISTRY_SUCCESS.getCode() == registryResponse.getCode()) {
//            return nameServerNettyRemoteClient;
//        }
//        throw new RuntimeException("build name server remote client error");
//    }

}

