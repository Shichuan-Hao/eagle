package cn.byteswalk.eaglemq.nameserver.event;

import cn.byteswalk.eaglemq.common.utils.ReflectUtils;
import cn.byteswalk.eaglemq.nameserver.event.spi.listener.Listener;
import cn.byteswalk.eaglemq.nameserver.event.model.Event;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
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

    // 使用泛型确保类型安全
//    private static final Map<Class<? extends Event>, List<Listener<? extends Event>>> eventListenerMap = new ConcurrentHashMap<>();
    private static final Map<Class<? extends Event>, List<Listener<? extends Event>>> eventListenerMap = Maps.newConcurrentMap();

    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
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

    // 注册事件 event 对应的 listener
    @SuppressWarnings("unchecked")
    public <E extends Event> void init() {
//        registry(clazz, listener);
        // SPI 机制，JDK内置的一种提供基于文件管理接口实现的方式
        //spi机制，jdk内置的一种提供基于文件管理接口实现的方式
        ServiceLoader<Listener> serviceLoader = ServiceLoader.load(Listener.class);
        for (Listener<?> listener : serviceLoader) {
            Class clazz = ReflectUtils.getInterfaceT(listener,0);
            this.registry(clazz,listener);
        }
    }

    // 将监听器注册到事件类型上
    private <E extends Event> void registry(Class<E> clazz, Listener<E> listener) {
        // 可以使用 computeIfAbsent 来简化逻辑
        // eventListenerMap.computeIfAbsent(clazz, k -> new CopyOnWriteArrayList<>()).add(listener);
        List<Listener<? extends Event>> listeners = eventListenerMap.get(clazz);
        if (CollectionUtils.isEmpty(listeners)) {
            eventListenerMap.put(clazz, Lists.newArrayList());
        } else {
            listeners.add(listener);
            eventListenerMap.put(clazz, listeners);
        }
    }

    // 发布事件
    public <E extends Event> void publish(E event) {
        Class<?> eventClass = event.getClass();
        List<Listener<?>> listeners = eventListenerMap.get(eventClass);
        if (CollectionUtils.isNotEmpty(listeners)) {
            threadPoolExecutor.execute(() -> {
                try {
                    for (Listener<?> listener : listeners) {
                        // 进行类型安全的强制转换
                        @SuppressWarnings("unchecked")
                        Listener<E> typedListener = (Listener<E>) listener;
                        typedListener.onReceive(event);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public static void main(String[] args) {
//        ServiceLoader<Listener> serviceLoader = ServiceLoader.load(Listener.class);
//        for (Listener listener : serviceLoader) {
//            System.out.println(listener.getClass().getName());
//        }
        EventBus eventBus = new EventBus();
        eventBus.init();
    }
}


