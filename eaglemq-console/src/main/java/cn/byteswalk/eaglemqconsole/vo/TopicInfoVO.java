package cn.byteswalk.eaglemqconsole.vo;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-03 18:04
 * @Description:
 * @Version: 1.0
 */
public class TopicInfoVO {
    private String topic;

    private Integer queueCount;

    private Long createAt;

    private Integer page;

    private Integer pageSize;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Integer getQueueCount() {
        return queueCount;
    }

    public void setQueueCount(Integer queueCount) {
        this.queueCount = queueCount;
    }

    public Long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Long createAt) {
        this.createAt = createAt;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}

