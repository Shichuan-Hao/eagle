package cn.byteswalk.eaglemqbroker.model;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-03 13:34
 * @Description: ConsumerQueue 数据结构存储的最小单元对象
 * @Version: 1.0
 */
public class ConsumeQueueDetailModel {

    /**
     * commitLog 文件名称
     */
    private int commitLogFileName; // 4byte

    /**
     * 消息的位置
     */
    private int msgIndex;  // 4byte

    /**
     * 消息的长度
     */
    private int msgLength; // 4byte


    public int getCommitLogFileName() {
        return commitLogFileName;
    }

    public void setCommitLogFileName(int commitLogFileName) {
        this.commitLogFileName = commitLogFileName;
    }

    public int getMsgIndex() {
        return msgIndex;
    }

    public void setMsgIndex(int msgIndex) {
        this.msgIndex = msgIndex;
    }

    public int getMsgLength() {
        return msgLength;
    }

    public void setMsgLength(int msgLength) {
        this.msgLength = msgLength;
    }
}

