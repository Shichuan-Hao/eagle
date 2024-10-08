package com.byteswalk.eaglemq.broker.core;

import com.byteswalk.eaglemq.broker.model.EagleMqTopicModel;
import com.byteswalk.eaglemq.broker.model.QueueModel;
import com.byteswalk.eaglemq.broker.cache.CommonCache;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author idea
 * @Date: Created in 10:38 2024/4/21
 * @Description
 */
public class ConsumeQueueAppendHandler {

    public void prepareConsumeQueue(String topicName) throws IOException {
        EagleMqTopicModel eagleMqTopicModel = CommonCache.getEagleMqTopicModelMap().get(topicName);
        List<QueueModel> queueModelList = eagleMqTopicModel.getQueueList();
        List<ConsumeQueueMMapFileModel> consumeQueueMMapFileModels = new ArrayList<>();
        //循环遍历，mmap的初始化
        for (QueueModel queueModel : queueModelList) {
            ConsumeQueueMMapFileModel consumeQueueMMapFileModel = new ConsumeQueueMMapFileModel();
            consumeQueueMMapFileModel.loadFileInMMap(
                    topicName,
                    queueModel.getId(),
                    queueModel.getLastOffset(),
                    queueModel.getLatestOffset().get(),
                    queueModel.getOffsetLimit());
            consumeQueueMMapFileModels.add(consumeQueueMMapFileModel);
        }
        CommonCache.getConsumeQueueMMapFileModelManager().put(topicName,consumeQueueMMapFileModels);
    }
}
