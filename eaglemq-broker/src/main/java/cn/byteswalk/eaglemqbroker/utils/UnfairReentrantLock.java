package cn.byteswalk.eaglemqbroker.utils;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-02 11:22
 * @Description:
 * @Version: 1.0
 */
public class UnfairReentrantLock implements PutMessageLock{

   private final ReentrantLock reentrantLock =  new ReentrantLock();


    @Override
    public void lock() {
        reentrantLock.lock();
    }

    @Override
    public void unlock() {
        reentrantLock.unlock();

    }
}

