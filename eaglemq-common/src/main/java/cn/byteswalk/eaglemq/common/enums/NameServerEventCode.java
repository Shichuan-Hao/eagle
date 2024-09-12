package cn.byteswalk.eaglemq.common.enums;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-12 15:32
 * @Description: 注册中心事件码
 * @Version: 1.0
 */
public enum NameServerEventCode {

    REGISTRY(1, "注册事件"),
    UN_REGISTRY(2, "下线事件"),
    HEART_BEAT(3, "心跳事件"),
    ;

    int code;
    String desc;

    NameServerEventCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    // fromCode 方法
    public static NameServerEventCode fromCode(int code) {
        for (NameServerEventCode eventCode : NameServerEventCode.values()) {
            if (eventCode.getCode() == code) {
                return eventCode;
            }
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }

}

