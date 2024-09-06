package cn.byteswalk.eaglemq.common.dto;

import cn.byteswalk.eaglemq.common.model.ConsumeQueueOffsetModel;
import cn.byteswalk.eaglemq.common.model.TopicModel;

import java.util.List;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-05 18:14
 * @Description:
 * @Version: 1.0
 */
public class EagleMqBrokerDataDTO
        extends BaseRemoteDTO {
    private List<TopicModel> eagleMqTopicModelList;

    private ConsumeQueueOffsetModel consumeQueueOffsetModel;

    public List<TopicModel> getEagleMqTopicModelList() {
        return eagleMqTopicModelList;
    }

    public void setEagleMqTopicModelList(List<TopicModel> eagleMqTopicModelList) {
        this.eagleMqTopicModelList = eagleMqTopicModelList;
    }

    public ConsumeQueueOffsetModel getConsumeQueueOffsetModel() {
        return consumeQueueOffsetModel;
    }

    public void setConsumeQueueOffsetModel(ConsumeQueueOffsetModel consumeQueueOffsetModel) {
        this.consumeQueueOffsetModel = consumeQueueOffsetModel;
    }
}

