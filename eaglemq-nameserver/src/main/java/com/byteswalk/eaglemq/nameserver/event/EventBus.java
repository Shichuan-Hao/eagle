package com.byteswalk.eaglemq.nameserver.event;

import cn.byteswalk.eaglemq.common.utils.ReflectUtils;
import com.byteswalk.eaglemq.nameserver.event.model.Event;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import com.byteswalk.eaglemq.nameserver.event.spi.listener.Listener;

import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-12 11:40
 * @Description: 事件存储
 * - 1. 使用存储各个Event的订阅者集合
 * - 2.
 * @Version: 1.0
 */
public class EventBus {

    private static Map<Class<? extends Event>, List<Listener>> eventListenerMap = new ConcurrentHashMap<>();

    private String taskName = "event-bus-task-";

    public EventBus(String taskName) {
        this.taskName = taskName;
    }

    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            10,
            100,
            3,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(1000),
            r -> {
                Thread thread = new Thread(r);
                thread.setName("event-bus-task-" + UUID.randomUUID().toString());
                return thread;
            });


    public void init() {
        //spi机制，jdk内置的一种提供基于文件管理接口实现的方式
        ServiceLoader<Listener> serviceLoader = ServiceLoader.load(Listener.class);
        for (Listener listener : serviceLoader) {
            Class clazz = ReflectUtils.getInterfaceT(listener, 0);
            this.registry(clazz, listener);
        }
    }

    private <E extends Event> void registry(Class<? extends Event> clazz, Listener<E> listener) {
        List<Listener> listeners = eventListenerMap.get(clazz);
        if (CollectionUtils.isEmpty(listeners)) {
            eventListenerMap.put(clazz, Lists.newArrayList(listener));
        } else {
            listeners.add(listener);
            eventListenerMap.put(clazz, listeners);
        }
    }

    public void publish(Event event) {
        List<Listener> listeners = eventListenerMap.get(event.getClass());
        threadPoolExecutor.execute(() -> {
            try {
                for (Listener listener : listeners) {
                    listener.onReceive(event);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
