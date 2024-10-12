package com.byteswalk.eaglemq.nameserver;

import com.byteswalk.eaglemq.nameserver.common.CommonCache;
import com.byteswalk.eaglemq.nameserver.core.InValidServiceRemoveTask;
import com.byteswalk.eaglemq.nameserver.core.NameServerStarter;

import java.io.IOException;

/**
 * @Author idea
 * @Date: Created in 15:59 2024/5/2
 * @Description 注册中心启动类
 */
public class NameServerStartUp {

//    private static NameServerStarter nameServerStarter;

    public static void main(String[] args) throws InterruptedException, IOException {
        // 获取到了集群复制的配置属性
        CommonCache.getPropertiesLoader().loadProperties();

        // master-slave复制 ？ trace 复制
        // - master-salve
        // -- 对于 master 角色
        // --- 1. 开启一个额外的Netty进程
        // --- 2. slave 链接接入
        // --- 3. 当数据写入master的时候，将写入的数据同步给slave节点
        // -- 对于 slave 角色
        // --- 1. 开启一个额外的Netty进程
        // --- 2. slave端去链接master节点

        new Thread(new InValidServiceRemoveTask()).start();

        NameServerStarter nameServerStarter = new NameServerStarter(CommonCache.getNameserverProperties().getNameserverPort());
        // 阻塞
        nameServerStarter.startServer();
    }
}
