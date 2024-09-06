package cn.byteswalk.eaglemq.common.enums;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-06 10:58
 * @Description: 向注册中心 NameServer 注册的节点类型
 * @Version: 1.0
 */
public enum RegistryTypeEnum {


    PRODUCER("producer"),
    CONSUMER("consumer"),
    BROKER("broker")
    ;
    String code;

    RegistryTypeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }


}

