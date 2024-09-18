package cn.byteswalk.eaglemq.broker.netty.nameserver;

import cn.byteswalk.eaglemq.broker.cache.CommonCache;
import cn.byteswalk.eaglemq.common.coder.TcpMsg;
import cn.byteswalk.eaglemq.common.enums.NameServerEventCode;
import io.netty.channel.Channel;

import java.util.concurrent.TimeUnit;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-18 17:03
 * @Description: HeartBeatRequestTask
 * @Version: 1.0
 */
public class HeartBeatRequestTask implements Runnable {

    @Override
    @SuppressWarnings("InfiniteLoopStatement")
    public void run() {
        while (true) {
            try {
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

