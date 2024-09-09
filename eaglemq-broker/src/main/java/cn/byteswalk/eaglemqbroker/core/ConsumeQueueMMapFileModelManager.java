package cn.byteswalk.eaglemqbroker.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-04 10:40
 * @Description: ConsumeQueue 的 mmap 映射对象的管理器
 * @Version: 1.0
 */
public class ConsumeQueueMMapFileModelManager {


    private final Map<String, List<ConsumeQueueMMapFileModel>> consumeQueueMMapFileModelMap = new HashMap<>();

    public void  put(String topic, List<ConsumeQueueMMapFileModel> consumeQueueMMapFileModels) {
        consumeQueueMMapFileModelMap.put(topic, consumeQueueMMapFileModels);
    }

    public List<ConsumeQueueMMapFileModel> get(String topic) {
        return consumeQueueMMapFileModelMap.get(topic);
    }
}

