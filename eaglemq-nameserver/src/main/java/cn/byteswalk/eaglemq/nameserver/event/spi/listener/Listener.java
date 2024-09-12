package cn.byteswalk.eaglemq.nameserver.event.spi.listener;

import cn.byteswalk.eaglemq.nameserver.event.model.Event;

public interface Listener<E extends Event>{


    /**
     * 回调通知
     * @param event 事件
     */
    void onReceive(E event);
}
