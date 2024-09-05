package cn.byteswalk.eaglemq.utils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-02 11:26
 * @Description: 间隙锁
 * @Version: 1.0
 */
public class SpainLock implements PutMessageLock {

    AtomicInteger atomicInteger = new AtomicInteger();

    @Override
    public void lock() {
        do {
            int result = atomicInteger.getAndIncrement();
            if (result == 1) {
                return;
            }
        } while (true);
    }

    @Override
    public void unlock() {
        atomicInteger.decrementAndGet();
    }

}

