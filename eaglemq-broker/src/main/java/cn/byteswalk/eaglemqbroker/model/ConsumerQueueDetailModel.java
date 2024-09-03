package cn.byteswalk.eaglemqbroker.model;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-03 13:34
 * @Description: ConsumerQueue 数据结构存储的最小单元对象
 * @Version: 1.0
 */
public class ConsumerQueueDetailModel {

    /**
     * commitLog 文件名称
     */
    private String commitLogFileName;

    /**
     * 消息的位置
     */
    private long msgIndex;

    /**
     * 消息的长度
     */
    private int msgLength;


    public String getCommitLogFileName() {
        return commitLogFileName;
    }

    public void setCommitLogFileName(String commitLogFileName) {
        this.commitLogFileName = commitLogFileName;
    }

    public long getMsgIndex() {
        return msgIndex;
    }

    public void setMsgIndex(long msgIndex) {
        this.msgIndex = msgIndex;
    }

    public int getMsgLength() {
        return msgLength;
    }

    public void setMsgLength(int msgLength) {
        this.msgLength = msgLength;
    }
}

