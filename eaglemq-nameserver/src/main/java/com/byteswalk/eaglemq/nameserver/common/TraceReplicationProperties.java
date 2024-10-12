package com.byteswalk.eaglemq.nameserver.common;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-10-09 14:42
 * @Description: 链路复制集群模式配置属性类
 * @Version: 1.0
 */
public class TraceReplicationProperties {

    private String nextNode;

    public String getNextNode() {
        return nextNode;
    }

    public void setNextNode(String nextNode) {
        this.nextNode = nextNode;
    }
}

