package cn.byteswalk.eaglemq.broker.config;

/**
 * 全局属性
 */
public class GlobalProperties {

    /**
     * 读取环境变量中配置的mq存储绝对路径地址
     */
    private String eagleMqHome;

    /**注册中心相关的属性**/
    private String nameserverIp;
    private Integer nameserverPort;
    private String nameserverUser;
    private String nameserverPassword;


    public String getEagleMqHome() {
        return eagleMqHome;
    }

    public void setEagleMqHome(String eagleMqHome) {
        this.eagleMqHome = eagleMqHome;
    }

    public String getNameserverIp() {
        return nameserverIp;
    }

    public void setNameserverIp(String nameserverIp) {
        this.nameserverIp = nameserverIp;
    }

    public Integer getNameserverPort() {
        return nameserverPort;
    }

    public void setNameserverPort(Integer nameserverPort) {
        this.nameserverPort = nameserverPort;
    }

    public String getNameserverUser() {
        return nameserverUser;
    }

    public void setNameserverUser(String nameserverUser) {
        this.nameserverUser = nameserverUser;
    }

    public String getNameserverPassword() {
        return nameserverPassword;
    }

    public void setNameserverPassword(String nameserverPassword) {
        this.nameserverPassword = nameserverPassword;
    }
}
