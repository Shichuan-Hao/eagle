package cn.byteswalk.eaglemq.common.dto;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-05 17:51
 * @Description: 注册中心服务注册响应DTO基类
 * @Version: 1.0
 */
public class BaseNameServerRemoteDTO {
    private String msgId;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}

