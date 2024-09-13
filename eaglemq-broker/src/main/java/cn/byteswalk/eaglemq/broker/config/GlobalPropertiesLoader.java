package cn.byteswalk.eaglemq.broker.config;

import cn.byteswalk.eaglemq.common.constants.BrokerConstants;
import cn.byteswalk.eaglemq.broker.cache.CommonCache;

import java.util.Objects;

/**
 * 全局属性抽象类
 */
public class GlobalPropertiesLoader {


    public void loadProperties() {
        GlobalProperties globalProperties = new GlobalProperties();
        // 加载环境变量
        String eagleMqHome  = System.getenv(BrokerConstants.EAGLE_MQ_HOME);

        Objects.requireNonNull(eagleMqHome, "EAGLE_MQ_HOME is null!");

        globalProperties.setEagleMqHome(eagleMqHome);
        // 复制
        CommonCache.setGlobalProperties(globalProperties);
    }
}
