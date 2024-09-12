package cn.byteswalk.eaglemq.nameserver.event.spi.listener;

import cn.byteswalk.eaglemq.nameserver.event.model.HeartBeatEvent;

import java.util.List;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-12 17:50
 * @Description: 心跳
 * @Version: 1.0
 */
public class HeartBeatListener
        implements Listener<HeartBeatEvent> {
    @Override
    public void onReceive(HeartBeatEvent event) {

    }
}

