package cn.byteswalk.eaglemq.common.enums;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-12 15:32
 * @Description: 请求注册中心nameserver的响应码
 * @Version: 1.0
 */
public enum NameServerRespCode {

    ERROR_USER_OR_PASSWORD(1001, "账号验证异常"),
    UNREGISTRY_SERVICE(1002, "服务正常下线"),
    REGISTRY_SUCCESS(1003, "注册成功"),
    ;

    final int code;
    final String desc;

    NameServerRespCode(int code, String desc) {
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
    public static NameServerRespCode fromCode(int code) {
        for (NameServerRespCode eventCode : NameServerRespCode.values()) {
            if (eventCode.getCode() == code) {
                return eventCode;
            }
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }

}

