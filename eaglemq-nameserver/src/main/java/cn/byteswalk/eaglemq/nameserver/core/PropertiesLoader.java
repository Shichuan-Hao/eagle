package cn.byteswalk.eaglemq.nameserver.core;

import cn.byteswalk.eaglemq.common.constants.BrokerConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
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

    private final Logger logger = LoggerFactory.getLogger(PropertiesLoader.class);

    private Properties properties;

    public void loadProperties() {
        String eagleMqHome = System.getenv(BrokerConstants.EAGLE_MQ_HOME);
        properties = new Properties();
        try {
            properties.load(Files.newInputStream(new File(eagleMqHome + "/broker/config/nameserver.properties").toPath()));
        } catch (IOException e) {
            logger.error("Failed to load properties file", e);
        }
    }

    public String getProperties(String key) {
        return properties.getProperty(key);
    }

}

