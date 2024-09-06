package cn.byteswalk.eaglemqconsole.vo;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-06 13:55
 * @Description: TopicListResultVO
 * @Version: 1.0
 */
public class TopicListResultVO {

    private String topic;
    private Integer queueSize;
    private Integer currentOffset;
    private Long offsetLimit;
    private String createTime;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Integer getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(Integer queueSize) {
        this.queueSize = queueSize;
    }

    public Integer getCurrentOffset() {
        return currentOffset;
    }

    public void setCurrentOffset(Integer currentOffset) {
        this.currentOffset = currentOffset;
    }

    public Long getOffsetLimit() {
        return offsetLimit;
    }

    public void setOffsetLimit(Long offsetLimit) {
        this.offsetLimit = offsetLimit;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}

