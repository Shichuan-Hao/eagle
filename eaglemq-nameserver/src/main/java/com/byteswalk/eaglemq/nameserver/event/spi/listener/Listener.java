package com.byteswalk.eaglemq.nameserver.event.spi.listener;

import com.byteswalk.eaglemq.nameserver.event.model.Event;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-12 17:50
 * @Description: 事件监听器
 * @Version: 1.0
 */
public interface Listener<E extends Event> {

    /**
     * 回调通知
     *
     * @param event
     */
    void onReceive(E event) throws Exception;
}
