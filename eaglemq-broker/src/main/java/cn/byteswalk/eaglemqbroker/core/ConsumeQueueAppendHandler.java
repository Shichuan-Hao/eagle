package cn.byteswalk.eaglemqbroker.core;

import cn.byteswalk.eaglemqbroker.cache.CommonCache;
import cn.byteswalk.eaglemqbroker.model.QueueModel;
import cn.byteswalk.eaglemqbroker.model.TopicModel;

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
                    queueModel.getOffsetLimit());
            consumeQueueMMapFileModels.add(consumeQueueMMapFileModel);
        }
        CommonCache.getConsumeQueueMMapFileModelManager().put(topicName, consumeQueueMMapFileModels);
    }

    /**
     * 读消息
     * @param topicName 主题名称
     * @return 返回读取的消息内容
     */
    public String readMsg(String topicName) {
        List<ConsumeQueueMMapFileModel> consumeQueueMMapFileModels = CommonCache.getConsumeQueueMMapFileModelManager().get(topicName);
        checkConsumeQueueMMapFileIsNull(consumeQueueMMapFileModels);
        return null;
    }

    /**
     * 像主题文件中追加写
     * @param topicName 主题名称
     * @param content 追加写的内容
     */
    public void appendMsg(String topicName, byte[] content)
            throws IOException {
        List<ConsumeQueueMMapFileModel> consumeQueueMMapFileModels = CommonCache.getConsumeQueueMMapFileModelManager().get(topicName);

        checkConsumeQueueMMapFileIsNull(consumeQueueMMapFileModels);

//        CommitLogMessageModel commitLogMessageModel = new CommitLogMessageModel();
//        commitLogMessageModel.setContent(content);
//        mMapFileModel.writeContent(commitLogMessageModel);
    }

    /**
     * 判断文件内存映射对象是否胃口，如果为空则抛出“Topic is invalid!”异常信息
     * @param consumeQueueMMapFileModels 文件内存映射对象
     */
    private void checkConsumeQueueMMapFileIsNull(List<ConsumeQueueMMapFileModel> consumeQueueMMapFileModels) {
        if (consumeQueueMMapFileModels == null || consumeQueueMMapFileModels.isEmpty()) {
            throw new RuntimeException("Topic is invalid!");
        }
    }
}

