package cn.byteswalk.eaglemq.nameserver.event.model;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-12 15:40
 * @Description: 注册事件（首次注册到注册中心使用）
 * @Version: 1.0
 */
public class RegistryEvent
        extends Event {

    private String brokerIp;
    private Integer brokerPort;
    private String user;
    private String password;


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBrokerIp() {
        return brokerIp;
    }

    public void setBrokerIp(String brokerIp) {
        this.brokerIp = brokerIp;
    }

    public Integer getBrokerPort() {
        return brokerPort;
    }

    public void setBrokerPort(Integer brokerPort) {
        this.brokerPort = brokerPort;
    }
}

