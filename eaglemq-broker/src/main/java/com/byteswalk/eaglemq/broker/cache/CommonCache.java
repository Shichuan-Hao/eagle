package com.byteswalk.eaglemq.broker.cache;

import com.byteswalk.eaglemq.broker.core.CommitLogMMapFileModelManager;
import com.byteswalk.eaglemq.broker.model.ConsumeQueueOffsetModel;
import com.byteswalk.eaglemq.broker.model.EagleMqTopicModel;
import com.byteswalk.eaglemq.broker.config.GlobalProperties;
import com.byteswalk.eaglemq.broker.core.ConsumeQueueMMapFileModelManager;
import com.byteswalk.eaglemq.broker.netty.nameserver.HeartBeatTaskManager;
import com.byteswalk.eaglemq.broker.netty.nameserver.NameServerClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author idea
 * @Date: Created in 08:59 2024/3/26
 * @Description 统一缓存对象
 */
public class CommonCache {

    private static NameServerClient nameServerClient = new NameServerClient();
    private static GlobalProperties globalProperties = new GlobalProperties();
    private static List<EagleMqTopicModel> eagleMqTopicModelList = new ArrayList<>();
    private static ConsumeQueueOffsetModel consumeQueueOffsetModel = new ConsumeQueueOffsetModel();
    private static ConsumeQueueMMapFileModelManager consumeQueueMMapFileModelManager = new ConsumeQueueMMapFileModelManager();
    private static CommitLogMMapFileModelManager commitLogMMapFileModelManager = new CommitLogMMapFileModelManager();
    private static HeartBeatTaskManager heartBeatTaskManager = new HeartBeatTaskManager();

    public static HeartBeatTaskManager getHeartBeatTaskManager() {
        return heartBeatTaskManager;
    }

    public static void setHeartBeatTaskManager(HeartBeatTaskManager heartBeatTaskManager) {
        CommonCache.heartBeatTaskManager = heartBeatTaskManager;
    }

    public static NameServerClient getNameServerClient() {
        return nameServerClient;
    }

    public static void setNameServerClient(NameServerClient nameServerClient) {
        CommonCache.nameServerClient = nameServerClient;
    }

    public static CommitLogMMapFileModelManager getCommitLogMMapFileModelManager() {
        return commitLogMMapFileModelManager;
    }

    public static void setCommitLogMMapFileModelManager(CommitLogMMapFileModelManager commitLogMMapFileModelManager) {
        CommonCache.commitLogMMapFileModelManager = commitLogMMapFileModelManager;
    }

    public static ConsumeQueueMMapFileModelManager getConsumeQueueMMapFileModelManager() {
        return consumeQueueMMapFileModelManager;
    }

    public static void setConsumeQueueMMapFileModelManager(ConsumeQueueMMapFileModelManager consumeQueueMMapFileModelManager) {
        CommonCache.consumeQueueMMapFileModelManager = consumeQueueMMapFileModelManager;
    }

    public static GlobalProperties getGlobalProperties() {
        return globalProperties;
    }

    public static void setGlobalProperties(GlobalProperties globalProperties) {
        CommonCache.globalProperties = globalProperties;
    }

    public static Map<String, EagleMqTopicModel> getEagleMqTopicModelMap() {
        return eagleMqTopicModelList.stream().collect(Collectors.toMap(EagleMqTopicModel::getTopic, item->item));
    }

    public static List<EagleMqTopicModel> getEagleMqTopicModelList() {
        return eagleMqTopicModelList;
    }

    public static void setEagleMqTopicModelList(List<EagleMqTopicModel> eagleMqTopicModelList) {
        CommonCache.eagleMqTopicModelList = eagleMqTopicModelList;
    }

    public static ConsumeQueueOffsetModel getConsumeQueueOffsetModel() {
        return consumeQueueOffsetModel;
    }

    public static void setConsumeQueueOffsetModel(ConsumeQueueOffsetModel consumeQueueOffsetModel) {
        CommonCache.consumeQueueOffsetModel = consumeQueueOffsetModel;
    }
}
