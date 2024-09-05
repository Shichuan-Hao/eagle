package cn.byteswalk.eaglemq.cache;

import cn.byteswalk.eaglemq.config.GlobalProperties;
import cn.byteswalk.eaglemq.model.TopicModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 统一缓存对象
 */
public class CommonCache {

    /**
     * 全局属性
     */
    private static GlobalProperties globalProperties = new GlobalProperties();

    /**
     * 主题模型
     *
     */
    private static List<TopicModel> topicModelList = new ArrayList<>();


    public static GlobalProperties getGlobalProperties() {
        return globalProperties;
    }

    public static void setGlobalProperties(GlobalProperties globalProperties) {
        CommonCache.globalProperties = globalProperties;
    }

    public static Map<String, TopicModel> getTopicModelMap() {
        return topicModelList.stream()
                .collect(Collectors.toMap(TopicModel::getTopicName, item -> item));
    }

    public static List<TopicModel> getTopicModelList() {
        return topicModelList;
    }

    public static void setTopicModelList(List<TopicModel> topicModelList) {
        CommonCache.topicModelList = topicModelList;
    }
}
