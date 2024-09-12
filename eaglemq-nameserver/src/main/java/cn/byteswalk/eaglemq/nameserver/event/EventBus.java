package cn.byteswalk.eaglemq.nameserver.event;

import cn.byteswalk.eaglemq.nameserver.event.listener.Listener;
import cn.byteswalk.eaglemq.nameserver.event.model.Event;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

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

    // 注册事件
    public <E extends Event> void init(Class<E> clazz, Listener<E> listener) {
//        registry(clazz, listener);
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
            for (Listener<?> listener : listeners) {
                // 进行类型安全的强制转换
                @SuppressWarnings("unchecked")
                Listener<E> typedListener = (Listener<E>) listener;
                typedListener.onReceive(event);
            }
        }
    }
}


