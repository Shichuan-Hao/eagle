package cn.byteswalk.eaglemq.nameserver;

import cn.byteswalk.eaglemq.nameserver.commom.CommonCache;
import cn.byteswalk.eaglemq.nameserver.core.NameServerStarter;
import cn.byteswalk.eaglemq.nameserver.core.InValidServiceRemoveTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-05 13:04
 * @Description: 注册中心启动类
 * @Version: 1.0
 */
public class NameServerStartUp {

    private static Logger logger = LoggerFactory.getLogger(NameServerStartUp.class);

    public static final int PORT = 9090;

    private static NameServerStarter nameServerStarter;

    public static void main(String[] args) {
        logger.info("{} Starting NameServer", NameServerStartUp.class.getSimpleName());
        CommonCache.getPropertiesLoader().loadProperties();
        new Thread(new InValidServiceRemoveTask()).start();
        nameServerStarter = new NameServerStarter(9090);
        try {
            nameServerStarter.startServer();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}

