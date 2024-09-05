package cn.byteswalk.eaglemq.common.dto;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-05 18:15
 * @Description:
 * @Version: 1.0
 */
public class BaseBrokerRemoteDTO {
    private String msgId;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}

