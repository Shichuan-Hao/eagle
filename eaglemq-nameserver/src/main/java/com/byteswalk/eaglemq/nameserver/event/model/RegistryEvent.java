package com.byteswalk.eaglemq.nameserver.event.model;

import com.alibaba.fastjson.JSON;

/**
 * @Author idea
 * @Date: Created in 14:19 2024/5/4
 * @Description 注册事件（首次链接nameserver使用）
 */
public class RegistryEvent extends Event{

    private String user;
    private String password;
    private String brokerIp;
    private Integer brokerPort;

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

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
