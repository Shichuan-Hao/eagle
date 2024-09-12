package cn.byteswalk.eaglemq.broker.core;

import cn.byteswalk.eaglemq.broker.cache.CommonCache;
import cn.byteswalk.eaglemq.broker.model.ConsumeQueueDetailModel;
import cn.byteswalk.eaglemq.broker.model.ConsumeQueueOffsetModel;
import cn.byteswalk.eaglemq.broker.model.QueueModel;
import cn.byteswalk.eaglemq.broker.model.TopicModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

   private static final Logger logger = LoggerFactory.getLogger(ConsumeQueueConsumeHandler.class);

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
        List<QueueModel> queueModels = topicModel.getQueueModels();
        if (queueOffsetDetailMap == null) {
            queueOffsetDetailMap = new HashMap<>();
            for (QueueModel queueModel : queueModels) {
                queueOffsetDetailMap.put(String.valueOf(queueModel.getId()), "00000000#0");
            }
            consumeGroupOffsetMap.put(consumeGroup, queueOffsetDetailMap);
        }
        String offsetStrInfo = queueOffsetDetailMap.get(String.valueOf(queueId));
        String[] offsetStrArr = offsetStrInfo.split("#");
        int consumeQueueOffset = Integer.parseInt(offsetStrArr[1]);
        QueueModel queueModel = queueModels.get(queueId);
        // 消费到了尽头
        if (queueModel.getLatestOffset().get() <= consumeQueueOffset) {
            return null;
        }
        // 3. 获取当前匹配的队列存储文件的 mmap 对象，然后读取 offset 地址的数据
        ConsumeQueueMMapFileModelManager consumeQueueMMapFileModelManager = CommonCache.getConsumeQueueMMapFileModelManager();
        List<ConsumeQueueMMapFileModel> consumeQueueMMapFileModels = consumeQueueMMapFileModelManager.get(topicName);
        // 每条队列的文件的 MMap 映射
        ConsumeQueueMMapFileModel consumeQueueMMapFileModel = consumeQueueMMapFileModels.get(queueId);
        byte[] content = consumeQueueMMapFileModel.readContent(consumeQueueOffset);
        ConsumeQueueDetailModel consumeQueueDetailModel = new ConsumeQueueDetailModel();
        consumeQueueDetailModel.buildFormBytes(content);

        CommitLogMMapFileModel commitLogMMapFileModel = CommonCache.getCommitLogMMapFileModelManager().get(topicName);
        return commitLogMMapFileModel.readContent(consumeQueueDetailModel.getMsgIndex(), consumeQueueDetailModel.getMsgLength());
    }

    /**
     * 更新consumeQueue-offset的值
     * @return ack记过
     */
    public boolean ack(String topicName, String consumeGroup, Integer queueId) {
        try {
            ConsumeQueueOffsetModel.OffsetTable offsetTable = CommonCache.getConsumeQueueOffsetModel().getOffsetTable();
            Map<String, ConsumeQueueOffsetModel.ConsumerGroupDetail> topicConsumerGroupDetail = offsetTable.getTopicConsumerGroupDetail();
            ConsumeQueueOffsetModel.ConsumerGroupDetail consumerGroupDetail = topicConsumerGroupDetail.get(topicName);
            Map<String, String> consumeQueueOffsetDetailMap = consumerGroupDetail.getConsumerGroupDetailMap().get(consumeGroup);
            String[] offsetStrArr = consumeQueueOffsetDetailMap.get(String.valueOf(queueId)).split("#");
            String fileName = offsetStrArr[0];
            int currentOffset = Integer.parseInt(offsetStrArr[1]);
            currentOffset += 12;
            consumeQueueOffsetDetailMap.put(String.valueOf(queueId), fileName + "#" + currentOffset);
            return true;
        } catch (Exception e) {
            logger.error("ack 操作异常");
            return false;
        }
    }
}

