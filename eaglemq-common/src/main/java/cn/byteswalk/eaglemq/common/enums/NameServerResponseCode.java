package cn.byteswalk.eaglemq.common.enums;

/**
 * @Author idea
 * @Date: Created in 17:31 2024/5/4
 * @Description 请求nameserver的响应码
 */
public enum NameServerResponseCode {

    ERROR_USER_OR_PASSWORD(1001,"账号验证异常"),
    UN_REGISTRY_SERVICE(1002,"服务正常下线"),
    REGISTRY_SUCCESS(1003,"注册成功"),
    ;


    final int code;
    final String desc;

    NameServerResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
