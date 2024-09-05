package cn.byteswalk.eaglemq.common.cache;

import cn.byteswalk.eaglemq.common.remote.SyncFuture;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-05 17:57
 * @Description:
 * @Version: 1.0
 */
public class NameServerSyncFutureManager {

    private static Map<String, SyncFuture> syncFutureMap = new ConcurrentHashMap<>();

    public static void put(String key, SyncFuture syncFuture) {
        syncFutureMap.put(key, syncFuture);
    }

    public static SyncFuture get(String key) {
        return syncFutureMap.get(key);
    }

    public static void remove(String key) {
        syncFutureMap.remove(key);
    }
}

