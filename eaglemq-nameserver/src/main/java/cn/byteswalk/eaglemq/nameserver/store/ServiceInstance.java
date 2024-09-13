package cn.byteswalk.eaglemq.nameserver.store;

import com.google.common.collect.Maps;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-13 10:39
 * @Description: 服务实例
 * @Version: 1.0
 */
public class ServiceInstance {

    private String brokerIp;
    private Integer brokerPort;
    /**
     * 首次注册时间
     */
    private Long firstRegistryTime;
    /**
     * 上一次的心跳时间
     */
    private Long lastHeartBeatTime;
    /**
     * 元数据信息，各种各样的扩展属性
     */
    private Map<String, String> attributes = Maps.newHashMap();

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public Long getLastHeartBeatTime() {
        return lastHeartBeatTime;
    }

    public void setLastHeartBeatTime(Long lastHeartBeatTime) {
        this.lastHeartBeatTime = lastHeartBeatTime;
    }

    public Long getFirstRegistryTime() {
        return firstRegistryTime;
    }

    public void setFirstRegistryTime(Long firstRegistryTime) {
        this.firstRegistryTime = firstRegistryTime;
    }

    public Integer getBrokerPort() {
        return brokerPort;
    }

    public void setBrokerPort(Integer brokerPort) {
        this.brokerPort = brokerPort;
    }

    public String getBrokerIp() {
        return brokerIp;
    }

    public void setBrokerIp(String brokerIp) {
        this.brokerIp = brokerIp;
    }

}

