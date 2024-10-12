package com.byteswalk.eaglemq.nameserver.common;

import com.byteswalk.eaglemq.nameserver.core.PropertiesLoader;
import com.byteswalk.eaglemq.nameserver.store.ReplicationChannelManager;
import com.byteswalk.eaglemq.nameserver.store.ServiceInstanceManager;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-13 14:30
 * @Description: 通用缓存
 * @Version: 1.0
 */
public class CommonCache {

    private static ServiceInstanceManager serviceInstanceManager = new ServiceInstanceManager();
    private static PropertiesLoader propertiesLoader = new PropertiesLoader();
    private static NameserverProperties nameserverProperties = new NameserverProperties();
    private static ReplicationChannelManager replicationChannelManager = new ReplicationChannelManager();

    public static ServiceInstanceManager getServiceInstanceManager() {
        return serviceInstanceManager;
    }

    public static void setServiceInstanceManager(ServiceInstanceManager serviceInstanceManager) {
        CommonCache.serviceInstanceManager = serviceInstanceManager;
    }

    public static PropertiesLoader getPropertiesLoader() {
        return propertiesLoader;
    }

    public static void setPropertiesLoader(PropertiesLoader propertiesLoader) {
        CommonCache.propertiesLoader = propertiesLoader;
    }

    public static NameserverProperties getNameserverProperties() {
        return nameserverProperties;
    }

    public static void setNameserverProperties(NameserverProperties nameserverProperties) {
        CommonCache.nameserverProperties = nameserverProperties;
    }

    public static ReplicationChannelManager getReplicationChannelManager() {
        return replicationChannelManager;
    }

    public static void setReplicationChannelManager(ReplicationChannelManager replicationChannelManager) {
        CommonCache.replicationChannelManager = replicationChannelManager;
    }
}
