package cn.byteswalk.eaglemq.broker.model;

import java.util.List;

/**
 * mq的topic映射对象
 */
public class TopicModel {

    private String topicName;
    private CommitLogModel commitLogModel;
    private List<QueueModel> queueModels;
    private Long createAt;
    private Long updateAt;

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public CommitLogModel getCommitLogModel() {
        return commitLogModel;
    }

    public void setCommitLogModel(CommitLogModel commitLogModel) {
        this.commitLogModel = commitLogModel;
    }

    public List<QueueModel> getQueueModels() {
        return queueModels;
    }

    public void setQueueModels(List<QueueModel> queueModels) {
        this.queueModels = queueModels;
    }

    public Long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Long createAt) {
        this.createAt = createAt;
    }

    public Long getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Long updateAt) {
        this.updateAt = updateAt;
    }

    @Override
    public String toString() {
        return "TopicModel{" +
                "topic='" + topicName + '\'' +
                ", commitLogModel=" + commitLogModel +
                ", queueList=" + queueModels +
                ", createTime=" + createAt +
                ", updateTime=" + updateAt +
                '}';
    }
}
