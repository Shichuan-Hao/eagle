package cn.byteswalk.eaglemq.nameserver.common;

import cn.byteswalk.eaglemq.nameserver.core.PropertiesLoader;
import cn.byteswalk.eaglemq.nameserver.store.ServiceInstanceManager;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-13 14:30
 * @Description: 通用缓存
 * @Version: 1.0
 */
public class CommonCache {

    private static ServiceInstanceManager serviceInstanceManager = new ServiceInstanceManager();
    private static PropertiesLoader propertiesLoader = new PropertiesLoader();

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
}
