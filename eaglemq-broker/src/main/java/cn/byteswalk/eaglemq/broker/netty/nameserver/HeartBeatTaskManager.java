package cn.byteswalk.eaglemq.broker.netty.nameserver;

import cn.byteswalk.eaglemq.broker.cache.CommonCache;
import cn.byteswalk.eaglemq.common.coder.TcpMsg;
import cn.byteswalk.eaglemq.common.enums.NameServerEventCode;
import io.netty.channel.Channel;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-18 17:02
 * @Description: 心跳数据上报
 * @Version: 1.0
 */
public class HeartBeatTaskManager {

    private AtomicInteger flag = new AtomicInteger(0);

    // 开启心跳传输任务
    public void startTask() {
        // 大于1代表已经触发过任务，可以保证有且只有一个心跳线程
        if (flag.getAndIncrement() > 1) {
            return;
        }
        Thread heartBeatRequestTask = new Thread(new HeartBeatTask());
        heartBeatRequestTask.setName("heart-beat-request-task");
        heartBeatRequestTask.start();
    }


    private class HeartBeatTask implements Runnable {
        @Override
        @SuppressWarnings("InfiniteLoopStatement")
        public void run() {
            while (true) {
                try {
                    // 每隔3秒发送一次心跳
                    TimeUnit.SECONDS.sleep(3);
                    //心跳包不需要额外透传过多的参数，只需要告诉nameserver这个channel依然存活即可
                    Channel channel = CommonCache.getNameServerClient().getChannel();
                    TcpMsg tcpMsg = new TcpMsg(NameServerEventCode.HEART_BEAT.getCode(),new byte[]{});
                    channel.writeAndFlush(tcpMsg);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

}

