package cn.byteswalk.eaglemqbroker.cache;

import cn.byteswalk.eaglemqbroker.config.GlobalProperties;
import cn.byteswalk.eaglemqbroker.core.ConsumeQueueMMapFileModelManager;
import cn.byteswalk.eaglemqbroker.model.ConsumeQueueOffsetModel;
import cn.byteswalk.eaglemqbroker.model.TopicModel;

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

    /**
     * ConsumeQueueOffset
     */
    private static ConsumeQueueOffsetModel consumeQueueOffsetModel = new ConsumeQueueOffsetModel();

    /**
     * ConsumeQueueMMapFileModelManager
     */
    private static ConsumeQueueMMapFileModelManager consumeQueueMMapFileModelManager = new ConsumeQueueMMapFileModelManager();

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

    public static ConsumeQueueOffsetModel getConsumeQueueOffsetModel() {
        return consumeQueueOffsetModel;
    }

    public static void setConsumeQueueOffsetModel(ConsumeQueueOffsetModel consumeQueueOffsetModel) {
        CommonCache.consumeQueueOffsetModel = consumeQueueOffsetModel;
    }

    public static ConsumeQueueMMapFileModelManager getConsumeQueueMMapFileModelManager() {
        return consumeQueueMMapFileModelManager;
    }

    public static void setConsumeQueueMMapFileModelManager(ConsumeQueueMMapFileModelManager consumeQueueMMapFileModelManager) {
        CommonCache.consumeQueueMMapFileModelManager = consumeQueueMMapFileModelManager;
    }
}
