package cn.byteswalk.eaglemq.common.dto;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-05 17:51
 * @Description: 服务注册响应 DTO
 * @Version: 1.0
 */
public class ServiceRegistryRespDTO
        extends BaseNameServerRemoteDTO {
    private String msgId;

    @Override
    public String getMsgId() {
        return msgId;
    }

    @Override
    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}

