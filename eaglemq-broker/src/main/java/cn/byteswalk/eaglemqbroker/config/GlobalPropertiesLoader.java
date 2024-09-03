package cn.byteswalk.eaglemqbroker.config;

import cn.byteswalk.eaglemqbroker.cache.CommonCache;
import cn.byteswalk.eaglemqbroker.constants.BrokerConstants;

import io.netty.util.internal.StringUtil;

/**
 * 全局属性抽象类
 */
public class GlobalPropertiesLoader {


    public void loadProperties() {
        GlobalProperties globalProperties = new GlobalProperties();
        // 加载环境变量
        String eagleMqHome  = System.getenv(BrokerConstants.EAGLE_MQ_HOME);
        if (StringUtil.isNullOrEmpty(eagleMqHome)) {
            throw new IllegalArgumentException("EAGLE_MQ_HOME is null!");
        }
        globalProperties.setEagleMqHome(eagleMqHome);
        // 复制
        CommonCache.setGlobalProperties(globalProperties);
    }
}
