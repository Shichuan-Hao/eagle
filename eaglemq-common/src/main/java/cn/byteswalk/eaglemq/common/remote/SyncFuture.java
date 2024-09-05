package cn.byteswalk.eaglemq.common.remote;

import cn.byteswalk.eaglemq.common.cache.NameServerSyncFutureManager;

import java.util.Objects;
import java.util.concurrent.*;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-05 17:58
 * @Description:
 * @Version: 1.0
 */
public class SyncFuture implements Future {

    /**
     * 远程调用 rpc 返回的数据内容
     */
    private Object response;
    /**
     * 远程调用 rpc 返回返回的数据标识id
     */
    private String msgId;


    private CountDownLatch countDownLatch = new CountDownLatch(1);

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
        countDownLatch.countDown();
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return response != null;
    }

    @Override
    public Object get()
            throws InterruptedException, ExecutionException {
        countDownLatch.await();
        NameServerSyncFutureManager.remove(msgId);
        return response;
    }

    @Override
    public Object get(long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        try {
            countDownLatch.await(timeout, unit);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            NameServerSyncFutureManager.remove(msgId);
        }
        return response;
    }
}

