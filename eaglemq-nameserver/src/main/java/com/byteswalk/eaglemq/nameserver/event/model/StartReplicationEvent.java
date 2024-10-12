package com.byteswalk.eaglemq.nameserver.event.model;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-10-09 17:04
 * @Description: 从节点首次连接主节点时候发送的事件
 * @Version: 1.0
 */
public class StartReplicationEvent extends Event{

    private String user;
    private String password;
    private String slaveIp;
    private String slavePort;

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

    public String getSlaveIp() {
        return slaveIp;
    }

    public void setSlaveIp(String slaveIp) {
        this.slaveIp = slaveIp;
    }

    public String getSlavePort() {
        return slavePort;
    }

    public void setSlavePort(String slavePort) {
        this.slavePort = slavePort;
    }

}

