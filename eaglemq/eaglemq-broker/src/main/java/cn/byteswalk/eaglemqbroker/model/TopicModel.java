package cn.byteswalk.eaglemqbroker.model;

import java.util.List;

/**
 * mq的topic映射对象
 */
public class TopicModel {
    private String topic;
    private CommitLogModel commitLogModel;
    private List<QueueModel> queueList;
    private Long createTime;
    private Long updateTime;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public CommitLogModel getCommitLogModel() {
        return commitLogModel;
    }

    public void setCommitLogModel(CommitLogModel commitLogModel) {
        this.commitLogModel = commitLogModel;
    }

    public List<QueueModel> getQueueList() {
        return queueList;
    }

    public void setQueueList(List<QueueModel> queueList) {
        this.queueList = queueList;
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
                "topic='" + topic + '\'' +
                ", commitLogModel=" + commitLogModel +
                ", queueList=" + queueList +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
