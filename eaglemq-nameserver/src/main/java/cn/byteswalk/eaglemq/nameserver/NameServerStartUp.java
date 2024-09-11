package cn.byteswalk.eaglemq.nameserver;

import cn.byteswalk.eaglemq.nameserver.core.NameServerStarter;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-05 13:04
 * @Description: 注册中心启动类
 * @Version: 1.0
 */
public class NameServerStartUp {

    public static final int PORT = 9090;

    private static NameServerStarter nameServerStarter;

    public static void main(String[] args) {
        nameServerStarter = new NameServerStarter(9090);
        try {
            nameServerStarter.startServer();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}

