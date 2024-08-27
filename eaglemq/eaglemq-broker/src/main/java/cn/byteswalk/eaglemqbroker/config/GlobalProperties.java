package cn.byteswalk.eaglemqbroker.config;

/**
 * 全局属性
 */
public class GlobalProperties {

    /**
     * 读取环境变量中配置的mq存储绝对路径地址
     */
    private String eagleMqHome;


    public String getEagleMqHome() {
        return eagleMqHome;
    }

    public void setEagleMqHome(String eagleMqHome) {
        this.eagleMqHome = eagleMqHome;
    }
}
