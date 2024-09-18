package cn.byteswalk.eaglemq.broker.netty.nameserver;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-18 17:02
 * @Description: 心跳数据上报
 * @Version: 1.0
 */
public class HeartBeatTaskManager {

    private final AtomicInteger flag = new AtomicInteger(0);

    // 开启心跳传输任务
    public void startTask() {
        if (flag.getAndIncrement() > 1) {
            return;
        }
        Thread heartBeatRequestTask = new Thread(new HeartBeatRequestTask());
        heartBeatRequestTask.setName("heart-beat-request-task");
        heartBeatRequestTask.start();
    }

}

