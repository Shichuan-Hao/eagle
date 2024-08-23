package cn.byteswalk.eaglemqbroker.cache;

import cn.byteswalk.eaglemqbroker.config.GlobalProperties;
import cn.byteswalk.eaglemqbroker.model.TopicModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 统一缓存对象
 */
public class CommonCache {

    private static GlobalProperties globalProperties = new GlobalProperties();
    private static List<TopicModel> topicModelList = new ArrayList<>();


    public static GlobalProperties getGlobalProperties() {
        return globalProperties;
    }

    public static void setGlobalProperties(GlobalProperties globalProperties) {
        CommonCache.globalProperties = globalProperties;
    }

    public static List<TopicModel> getTopicInfo() {
        return topicModelList;
    }

    public static void setTopicInfo(List<TopicModel> topicInfo) {
        CommonCache.topicModelList = topicInfo;
    }
}
