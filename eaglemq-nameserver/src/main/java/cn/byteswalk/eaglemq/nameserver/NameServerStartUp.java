package cn.byteswalk.eaglemq.nameserver;

import cn.byteswalk.eaglemq.nameserver.common.CommonCache;
import cn.byteswalk.eaglemq.nameserver.core.InValidServiceRemoveTask;
import cn.byteswalk.eaglemq.nameserver.core.NameServerStarter;

import java.io.IOException;

/**
 * @Author idea
 * @Date: Created in 15:59 2024/5/2
 * @Description 注册中心启动类
 */
public class NameServerStartUp {

    private static NameServerStarter nameServerStarter;

    public static void main(String[] args) throws InterruptedException, IOException {
        CommonCache.getPropertiesLoader().loadProperties();

        new Thread(new InValidServiceRemoveTask()).start();

        nameServerStarter = new NameServerStarter(9090);
        nameServerStarter.startServer();
    }
}
