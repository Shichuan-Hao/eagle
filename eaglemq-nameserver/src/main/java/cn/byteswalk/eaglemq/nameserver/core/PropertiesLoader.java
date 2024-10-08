package cn.byteswalk.eaglemq.nameserver.core;


import cn.byteswalk.eaglemq.common.constants.BrokerConstants;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-13 9:57
 * @Description: PropertiesLoader
 * @Version: 1.0
 */
public class PropertiesLoader {


    private Properties properties = new Properties();

    public void loadProperties() throws IOException {
        String eagleMqHome = System.getenv(BrokerConstants.EAGLE_MQ_HOME);
        properties.load(Files.newInputStream(new File(eagleMqHome + "/config/nameserver.properties").toPath()));
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
