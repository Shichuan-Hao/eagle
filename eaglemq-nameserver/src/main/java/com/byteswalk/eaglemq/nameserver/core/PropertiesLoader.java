package com.byteswalk.eaglemq.nameserver.core;


import cn.byteswalk.eaglemq.common.constants.BrokerConstants;
import com.byteswalk.eaglemq.nameserver.common.CommonCache;
import com.byteswalk.eaglemq.nameserver.common.MasterSlaveReplicationProperties;
import com.byteswalk.eaglemq.nameserver.common.NameserverProperties;
import com.byteswalk.eaglemq.nameserver.common.TraceReplicationProperties;

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
        // build nameserver properties
        this.buildNameserverProperties();
    }

    private void buildNameserverProperties() {
        NameserverProperties nameserverProperties = new NameserverProperties();
        nameserverProperties.setNameserverPwd(getStr("nameserver.password"));
        nameserverProperties.setNameserverUser(getStr("nameserver.user"));
        nameserverProperties.setNameserverPort(getInt("nameserver.port"));
        nameserverProperties.setReplicationMode(getStr("nameserver.replication.mode"));
        nameserverProperties.setTraceReplicationProperties(this.buildTraceReplicationProperties());
        nameserverProperties.setMasterSlaveReplicationProperties(this.buildMasterSlaveReplicationProperties());
        nameserverProperties.print();
        CommonCache.setNameserverProperties(nameserverProperties);
    }

    private TraceReplicationProperties buildTraceReplicationProperties() {
        TraceReplicationProperties traceReplicationProperties = new TraceReplicationProperties();
        traceReplicationProperties.setNextNode(getStrCanBeNull("nameserver.replication.next.node"));
        return traceReplicationProperties;
    }

    private MasterSlaveReplicationProperties buildMasterSlaveReplicationProperties() {
        MasterSlaveReplicationProperties masterSlaveReplicationProperties = new MasterSlaveReplicationProperties();
        masterSlaveReplicationProperties.setMaster(getStrCanBeNull("nameserver.replication.master"));
        masterSlaveReplicationProperties.setRole(getStrCanBeNull("nameserver.replication.master.slave.role"));
        masterSlaveReplicationProperties.setType(getStrCanBeNull("nameserver.replication.master.slave.type"));
        masterSlaveReplicationProperties.setPort(getInt("nameserver.replication.port"));
        return masterSlaveReplicationProperties;
    }


    private String getStrCanBeNull(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            return "";
        }
        return value;
    }

    private String getStr(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("配置参数：" + key + "不存在");
        }
        return value;
    }

    private Integer getInt(String key) {
        return Integer.valueOf(getStr(key));
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

}
