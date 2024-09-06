package cn.byteswalk.eaglemq.common.dto;

import cn.byteswalk.eaglemq.common.enums.RegistryTypeEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-06 10:56
 * @Description: 向注册中心NameServer进行服务注册时的请求DTO
 * @Version: 1.0
 */
public class ServiceRegistryReqDTO extends BaseRemoteDTO {

    /**
     * 节点注册类型，方便统计数据使用
     * @see RegistryTypeEnum
     */
    private String registryType;
    private String user;
    private String password;
    private String ip;
    private Integer id;

    // 后续会用到 todo
    private Map<String, Object> attr = new HashMap<>();

    public String getRegistryType() {
        return registryType;
    }

    public void setRegistryType(String registryType) {
        this.registryType = registryType;
    }

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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Map<String, Object> getAttr() {
        return attr;
    }

    public void setAttr(Map<String, Object> attr) {
        this.attr = attr;
    }
}

