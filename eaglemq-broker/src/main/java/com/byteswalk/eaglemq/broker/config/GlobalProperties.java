package com.byteswalk.eaglemq.broker.config;

/**
 * @Author idea
 * @Date: Created in 08:49 2024/3/26
 * @Description
 */
public class GlobalProperties {

    //nameserver的属性
    private String nameserverIp;
    private Integer nameserverPort;
    private String nameserverUser;
    private String nameserverPassword;
    private Integer brokerPort;

    /**
     * 读取环境变量中配置的mq存储绝对路径地址
     */
    private String eagleMqHome;

    public String getEagleMqHome() {
        return eagleMqHome;
    }

    public void setEagleMqHome(String eagleMqHome) {
        this.eagleMqHome = eagleMqHome;
    }

    public String getNameserverIp() {
        return nameserverIp;
    }

    public void setNameserverIp(String nameserverIp) {
        this.nameserverIp = nameserverIp;
    }

    public Integer getNameserverPort() {
        return nameserverPort;
    }

    public void setNameserverPort(Integer nameserverPort) {
        this.nameserverPort = nameserverPort;
    }

    public String getNameserverUser() {
        return nameserverUser;
    }

    public void setNameserverUser(String nameserverUser) {
        this.nameserverUser = nameserverUser;
    }

    public String getNameserverPassword() {
        return nameserverPassword;
    }

    public void setNameserverPassword(String nameserverPassword) {
        this.nameserverPassword = nameserverPassword;
    }

    public Integer getBrokerPort() {
        return brokerPort;
    }

    public void setBrokerPort(Integer brokerPort) {
        this.brokerPort = brokerPort;
    }
}
