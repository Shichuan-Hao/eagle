package cn.byteswalk.eaglemqbroker.cache;

import cn.byteswalk.eaglemqbroker.config.GlobalProperties;
import cn.byteswalk.eaglemqbroker.config.TopicInfo;

/**
 * 统一缓存对象
 */
public class CommonCache {

    private static GlobalProperties globalProperties = new GlobalProperties();
    private static TopicInfo topicInfo = new TopicInfo();


    public static GlobalProperties getGlobalProperties() {
        return globalProperties;
    }

    public static void setGlobalProperties(GlobalProperties globalProperties) {
        CommonCache.globalProperties = globalProperties;
    }

    public static TopicInfo getTopicInfo() {
        return topicInfo;
    }

    public static void setTopicInfo(TopicInfo topicInfo) {
        CommonCache.topicInfo = topicInfo;
    }
}
