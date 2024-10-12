package com.byteswalk.eaglemq.nameserver.store;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author idea
 * @Date: Created in 17:37 2024/5/4
 * @Description
 */
public class ServiceInstanceManager {

    private Map<String, ServiceInstance> serviceInstanceMap = new ConcurrentHashMap<>();

    public void putIfExist(ServiceInstance serviceInstance) {
        ServiceInstance currentInstance = this.get(serviceInstance.getBrokerIp(), serviceInstance.getBrokerPort());
        if (currentInstance != null && currentInstance.getFirstRegistryTime() != null) {
            serviceInstance.setFirstRegistryTime(currentInstance.getFirstRegistryTime());
        }
        serviceInstanceMap.put(serviceInstance.getBrokerIp() + ":" + serviceInstance.getBrokerPort(), serviceInstance);
    }

    public void put(ServiceInstance serviceInstance) {
        serviceInstanceMap.put(serviceInstance.getBrokerIp() + ":" + serviceInstance.getBrokerPort(), serviceInstance);
    }

    public ServiceInstance get(String brokerIp, Integer brokerPort) {
        return serviceInstanceMap.get(brokerIp + ":" + brokerPort);
    }

    public ServiceInstance remove(String key) {
        return serviceInstanceMap.remove(key);
    }

    public Map<String, ServiceInstance> getServiceInstanceMap() {
        return serviceInstanceMap;
    }
}
