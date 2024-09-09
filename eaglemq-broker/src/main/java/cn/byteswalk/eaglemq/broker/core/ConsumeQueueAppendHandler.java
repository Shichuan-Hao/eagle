package cn.byteswalk.eaglemq.broker.core;

import cn.byteswalk.eaglemq.broker.cache.CommonCache;
import cn.byteswalk.eaglemq.broker.model.QueueModel;
import cn.byteswalk.eaglemq.broker.model.TopicModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-04 10:43
 * @Description:
 * @Version: 1.0
 */
public class ConsumeQueueAppendHandler {

    /**
     *
     * @param topicName 消息主题名称
     * @throws IOException the exception to io
     */
    public void prepareMMapLoading(String topicName)
            throws IOException {
        TopicModel topicModel = CommonCache.getTopicModelMap().get(topicName);
        List<QueueModel> queueModels = topicModel.getQueueModels();
        List<ConsumeQueueMMapFileModel> consumeQueueMMapFileModels = new ArrayList<>();
        for (QueueModel queueModel : queueModels) {
            ConsumeQueueMMapFileModel consumeQueueMMapFileModel = new ConsumeQueueMMapFileModel();
            // 预加载
            consumeQueueMMapFileModel.loadFileInMMap(
                    topicName,
                    queueModel.getId(),
                    queueModel.getLastOffset(),
                    queueModel.getLatestOffset().get(),
                    queueModel.getOffsetLimit());
            consumeQueueMMapFileModels.add(consumeQueueMMapFileModel);
        }
        CommonCache.getConsumeQueueMMapFileModelManager().put(topicName, consumeQueueMMapFileModels);
    }

}

