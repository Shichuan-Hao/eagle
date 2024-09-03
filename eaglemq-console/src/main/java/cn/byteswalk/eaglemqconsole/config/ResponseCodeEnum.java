package cn.byteswalk.eaglemqconsole.config;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-03 18:06
 * @Description:
 * @Version: 1.0
 */
public enum ResponseCodeEnum {


    SUCCESS(0,"success"),
    FAIL(9999,"fail");

    private final int code;
    private final String desc;

    ResponseCodeEnum(int code, String desc) {
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

