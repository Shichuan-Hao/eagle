package cn.byteswalk.eaglemq.model;

import java.util.List;

/**
 * mq的topic映射对象
 */
public class TopicModel {
    private String topicName;
    private CommitLogModel commitLogModel;
    private List<QueueModel> queueModels;
    private Long createTime;
    private Long updateTime;

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

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "TopicModel{" +
                "topic='" + topicName + '\'' +
                ", commitLogModel=" + commitLogModel +
                ", queueList=" + queueModels +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
