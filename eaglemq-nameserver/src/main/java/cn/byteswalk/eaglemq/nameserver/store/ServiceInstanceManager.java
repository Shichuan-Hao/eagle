package cn.byteswalk.eaglemq.nameserver.store;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Objects;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-13 10:44
 * @Description: ServiceInstanceManager
 * @Version: 1.0
 */
public class ServiceInstanceManager {

    private final Map<String, ServiceInstance> serviceInstanceMap = Maps.newConcurrentMap();

    public void putIfExist(ServiceInstance serviceInstance) {
        String serviceInstanceMapKey = this.buildServiceInstanceMapKey(serviceInstance.getBrokerIp(), serviceInstance.getBrokerPort());
        ServiceInstance currentInstance = serviceInstanceMap.get(serviceInstanceMapKey);
        if (currentInstance != null && currentInstance.getFirstRegistryTime() != null) {
            serviceInstance.setFirstRegistryTime(currentInstance.getFirstRegistryTime());
        }
        serviceInstanceMap.put(serviceInstanceMapKey, serviceInstance);
    }

    public void put(ServiceInstance serviceInstance) {
        this.checkParams(serviceInstance);
        String serviceInstanceMapKey = this.buildServiceInstanceMapKey(serviceInstance.getBrokerIp(),
                serviceInstance.getBrokerPort());
        serviceInstanceMap.put(serviceInstanceMapKey, serviceInstance);
    }

    private String buildServiceInstanceMapKey(String serviceIp, int servicePort) {
        return serviceIp + ":" + servicePort;
    }

    private void checkParams(ServiceInstance serviceInstance) {
        String brokerIp = serviceInstance.getBrokerIp();
        Integer brokerPort = serviceInstance.getBrokerPort();
        Objects.requireNonNull(brokerIp, "brokerIp is null");
        Objects.requireNonNull(brokerPort, "brokerPort is null");
    }

    public boolean remove(String brokerIp, Integer brokerPort) {
        String serviceInstanceMapKey = this.buildServiceInstanceMapKey(brokerIp, brokerPort);
        return serviceInstanceMap.remove(serviceInstanceMapKey) != null;
    }
}

