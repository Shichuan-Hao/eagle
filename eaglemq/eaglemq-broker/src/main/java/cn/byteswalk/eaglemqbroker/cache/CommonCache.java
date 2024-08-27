package cn.byteswalk.eaglemqbroker.cache;

import cn.byteswalk.eaglemqbroker.config.GlobalProperties;
import cn.byteswalk.eaglemqbroker.model.TopicModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统一缓存对象
 */
public class CommonCache {

    /**
     * 全局属性
     */
    private static GlobalProperties globalProperties = new GlobalProperties();

    /**
     * 主题模型映射
     * Key:topicName:主题名称 Value:主题模型
     */
    private static Map<String, TopicModel> topicModelMap = new HashMap<>();


    public static GlobalProperties getGlobalProperties() {
        return globalProperties;
    }

    public static void setGlobalProperties(GlobalProperties globalProperties) {
        CommonCache.globalProperties = globalProperties;
    }

    public static Map<String, TopicModel> getTopicModelMap() {
        return topicModelMap;
    }

    public static void setTopicModelMap(Map<String, TopicModel> topicModelMap) {
        CommonCache.topicModelMap = topicModelMap;
    }
}
