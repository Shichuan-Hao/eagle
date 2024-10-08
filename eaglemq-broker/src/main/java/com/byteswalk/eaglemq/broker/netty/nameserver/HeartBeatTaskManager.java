package com.byteswalk.eaglemq.broker.netty.nameserver;

import com.byteswalk.eaglemq.broker.cache.CommonCache;
import io.netty.channel.Channel;
import cn.byteswalk.eaglemq.common.coder.TcpMsg;
import cn.byteswalk.eaglemq.common.enums.NameServerEventCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author idea
 * @Date: Created in 08:51 2024/5/12
 * @Description 心跳数据上报任务
 */
public class HeartBeatTaskManager {

    private final AtomicInteger flag = new AtomicInteger(0);

    private final Logger logger = LoggerFactory.getLogger(HeartBeatTaskManager.class);

    //开启心跳传输任务
    public void startTask() {
        if (flag.getAndIncrement() >= 1) {
            return;
        }
        Thread heartBeatRequestTask = new Thread(new HeartBeatRequestTask());
        heartBeatRequestTask.setName("heart-beat-request-task");
        heartBeatRequestTask.start();
    }


    private class HeartBeatRequestTask implements Runnable{
        @Override
        public void run() {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(3);
                    logger.info("向注册中心发送注册事件");
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
