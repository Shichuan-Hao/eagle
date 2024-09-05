package cn.byteswalk.eaglemq.common.dto;

import java.util.List;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-05 18:09
 * @Description: PullBrokerIpRespDTO
 * @Version: 1.0
 */
public class PullBrokerIpRespDTO
        extends BaseNameServerRemoteDTO {
    private List<String> addressList;
    private List<String> masterAddressList;
    private List<String> slaveAddressList;

    public List<String> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<String> addressList) {
        this.addressList = addressList;
    }

    public List<String> getMasterAddressList() {
        return masterAddressList;
    }

    public void setMasterAddressList(List<String> masterAddressList) {
        this.masterAddressList = masterAddressList;
    }

    public List<String> getSlaveAddressList() {
        return slaveAddressList;
    }

    public void setSlaveAddressList(List<String> slaveAddressList) {
        this.slaveAddressList = slaveAddressList;
    }
}

