package cn.byteswalk.eaglemq.broker.utils;


public interface PutMessageLock {

    /**
     * 加锁
     */
    void lock();

    /**
     * 解锁
     */
    void unlock();
}
