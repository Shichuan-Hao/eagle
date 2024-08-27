package cn.byteswalk.eaglemqbroker.config;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-08-27 17:34
 * @Description: 通用的线程池配置
 * @Version: 1.0
 */
public class CommonThreadPoolConfig {


    /**
     * 专门用于将 topic 配置异步刷盘使用
     */
    public static ThreadPoolExecutor refreshTopicExecutor = new ThreadPoolExecutor(1,
            1,
            30,
            TimeUnit.SECONDS, new ArrayBlockingQueue<>(10), r -> {
                Thread thread = new Thread(r);
                thread.setName("refresh-eagle-mq-topic-config");
                return thread;
            });
}

