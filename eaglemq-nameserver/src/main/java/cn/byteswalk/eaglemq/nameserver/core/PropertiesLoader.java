package cn.byteswalk.eaglemq.nameserver.core;

import cn.byteswalk.eaglemq.common.constants.BrokerConstants;
import cn.byteswalk.eaglemq.common.constants.NameServerConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
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
            String nameServerPropertyPath = eagleMqHome + NameServerConstants.PROPERTY_PATH;
            File file = new File(nameServerPropertyPath);
            Path path = file.toPath();
            InputStream inputStream = Files.newInputStream(path);
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error("Failed to load properties file", e);
        }
    }

    public String getProperties(String key) {
        return properties.getProperty(key);
    }

}

