package cn.byteswalk.eaglemq.common.dto;

import java.io.Serializable;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-18 17:09
 * @Description: RegistryDTO
 * @Version: 1.0
 */
public class RegistryDTO implements Serializable {

    private String user;
    private String password;
    private String brokerIp;
    private Integer brokerPort;
    private Long timeStamp;

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

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
