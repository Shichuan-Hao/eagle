package cn.byteswalk.eaglemq.nameserver.commom;

import cn.byteswalk.eaglemq.nameserver.core.PropertiesLoader;
import cn.byteswalk.eaglemq.nameserver.store.ServiceInstanceManager;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-13 10:02
 * @Description:
 * @Version: 1.0
 */
public class CommonCache {

    private static PropertiesLoader propertiesLoader = new PropertiesLoader();
    private static ServiceInstanceManager serviceInstanceManager = new ServiceInstanceManager();


    public static PropertiesLoader getPropertiesLoader() {
        return propertiesLoader;
    }

    public static void setPropertiesLoader(PropertiesLoader propertiesLoader) {
        CommonCache.propertiesLoader = propertiesLoader;
    }

    public static ServiceInstanceManager getServiceInstanceManager() {
        return serviceInstanceManager;
    }

    public static void setServiceInstanceManager(ServiceInstanceManager serviceInstanceManager) {
        CommonCache.serviceInstanceManager = serviceInstanceManager;
    }
}

