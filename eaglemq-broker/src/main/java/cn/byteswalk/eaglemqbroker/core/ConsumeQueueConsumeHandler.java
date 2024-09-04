package cn.byteswalk.eaglemqbroker.core;

import cn.byteswalk.eaglemqbroker.cache.CommonCache;
import cn.byteswalk.eaglemqbroker.model.ConsumeQueueOffsetModel;
import cn.byteswalk.eaglemqbroker.model.QueueModel;
import cn.byteswalk.eaglemqbroker.model.TopicModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-04 16:04
 * @Description: 消息队列消费处理器
 * @Version: 1.0
 */
public class ConsumeQueueConsumeHandler {

    /**
     * 读取当前最新一条ConsumeQueue的消息内容
     * @param topicName topic 名称
     * @param consumeGroup 消费组名称
     * @param queueId queueId
     * @return 返回消费的内容
     */
    public byte[] consume(String topicName, String consumeGroup, Integer queueId) {
        // 1. 检查参数合法性


        TopicModel topicModel = CommonCache.getTopicModelMap().get(topicName);
        if (topicModel == null) {
            throw new RuntimeException("topic " + topicName + "not exist!");
        }

        // 2. 获取当前匹配的队列的最新的 ConsumeQueue 的 offset 是多少
        ConsumeQueueOffsetModel.OffsetTable offsetTable = CommonCache.getConsumeQueueOffsetModel().getOffsetTable();
        Map<String, ConsumeQueueOffsetModel.ConsumerGroupDetail> topicConsumerGroupDetail = offsetTable.getTopicConsumerGroupDetail();
        ConsumeQueueOffsetModel.ConsumerGroupDetail consumerGroupDetail = topicConsumerGroupDetail.get(topicName);
        // 如果是首次消费
        if (consumerGroupDetail == null) {
            consumerGroupDetail = new ConsumeQueueOffsetModel.ConsumerGroupDetail();
            topicConsumerGroupDetail.put(topicName, consumerGroupDetail);
        }
        // key是消费组的名称
        Map<String, Map<String, String>> consumeGroupOffsetMap  = consumerGroupDetail.getConsumerGroupDetailMap();
        Map<String, String> queueOffsetDetailMap = consumeGroupOffsetMap .get(consumeGroup);
        if (queueOffsetDetailMap == null) {
            queueOffsetDetailMap = new HashMap<>();
            List<QueueModel> queueModels = topicModel.getQueueModels();
            for (QueueModel queueModel : queueModels) {
                queueOffsetDetailMap.put(String.valueOf(queueModel.getId()), "00000000#0");
            }
            consumeGroupOffsetMap.put(consumeGroup, queueOffsetDetailMap);
        }
        String offsetStrInfo = queueOffsetDetailMap.get(String.valueOf(queueId));
        String[] offsetStrArr = offsetStrInfo.split("#");
//        String consumeQueueFileName = offsetStrArr[0];
        int consumeQueueOffset = Integer.parseInt(offsetStrArr[1]);

        // 3. 获取当前匹配的队列存储文件的 mmap 对象，然后读取 offset 地址的数据
        ConsumeQueueMMapFileModelManager consumeQueueMMapFileModelManager = CommonCache.getConsumeQueueMMapFileModelManager();
        List<ConsumeQueueMMapFileModel> consumeQueueMMapFileModels = consumeQueueMMapFileModelManager.get(topicName);
        ConsumeQueueMMapFileModel consumeQueueMMapFileModel = consumeQueueMMapFileModels.get(queueId);
        byte[] content = consumeQueueMMapFileModel.readContent(consumeQueueOffset);

        return null;
    }

    /**
     * 更新consumeQueue-offset的值
     * @return
     */
    public boolean ack() {
        return true;
    }
}

