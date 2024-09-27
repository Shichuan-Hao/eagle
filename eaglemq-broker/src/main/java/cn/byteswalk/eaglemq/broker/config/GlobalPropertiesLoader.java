package cn.byteswalk.eaglemq.broker.config;

import cn.byteswalk.eaglemq.common.constants.BrokerConstants;
import cn.byteswalk.eaglemq.broker.cache.CommonCache;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

/**
 * 全局属性抽象类
 */
public class GlobalPropertiesLoader {


    public void loadProperties() {
        GlobalProperties globalProperties = new GlobalProperties();
        // 加载环境变量
        this.setEagleMqHome(globalProperties);
        // 加载注册中心相关的属性
        this.setNameserverProperties(globalProperties);
        // 复制
        CommonCache.setGlobalProperties(globalProperties);
    }

    private void setEagleMqHome(GlobalProperties globalProperties) {
        String eagleMqHome  = System.getenv(BrokerConstants.EAGLE_MQ_HOME);
        if (Objects.isNull(eagleMqHome)) {
            throw new IllegalArgumentException("EAGLE_MQ_HOME is null");
        }
        globalProperties.setEagleMqHome(eagleMqHome);
    }

    private void setNameserverProperties(GlobalProperties globalProperties) {
        try {
            Properties properties = new Properties();
            String eagleMqHome = globalProperties.getEagleMqHome();
            String brokerPropertiesPath = eagleMqHome  + BrokerConstants.BROKER_PROPERTIES_PATH;
            File file = new File(brokerPropertiesPath);
            FileInputStream fileInputStream = new FileInputStream(file);
            properties.load(fileInputStream);

            String nameserverIp         = properties.getProperty(BrokerConstants.NAMESERVER_IP);
            String nameserverPortStr    = properties.getProperty(BrokerConstants.NAMESERVER_PORT);
            String nameserverUser       = properties.getProperty(BrokerConstants.NAMESERVER_USER);
            String nameserverPassword   = properties.getProperty(BrokerConstants.NAMESERVER_PASSWORD);
            String brokerPortStr        = properties.getProperty(BrokerConstants.BROKER_PORT);

            globalProperties.setNameserverIp(nameserverIp);
            globalProperties.setNameserverPort(Integer.parseInt(nameserverPortStr));
            globalProperties.setNameserverUser(nameserverUser);
            globalProperties.setNameserverPassword(nameserverPassword);
            globalProperties.setBrokerPort(Integer.parseInt(brokerPortStr));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
