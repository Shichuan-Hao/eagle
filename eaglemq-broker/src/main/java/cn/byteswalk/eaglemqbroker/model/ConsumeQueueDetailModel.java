package cn.byteswalk.eaglemqbroker.model;

import cn.byteswalk.eaglemqbroker.utils.ByteConvertUtils;

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
     * CommitLog 数据存储的地址
     * mmap映射的地址，Integer.MAX校验
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

    public byte[] convertToBytes() {
        byte[] finalBytes = new byte[12];
        int p = 0;
        for (int i = 0; i < 4; i++) {
            finalBytes[p++] = ByteConvertUtils.intToBytes(this.commitLogFileName)[i];
        }
        for (int i = 0; i < 4; i++) {
            finalBytes[p++] = ByteConvertUtils.intToBytes(this.msgIndex)[i];
        }
        for (int i = 0; i < 4; i++) {
            finalBytes[p++] = ByteConvertUtils.intToBytes(this.msgLength)[i];
        }
        return finalBytes;
    }

    public ConsumeQueueDetailModel convertFromBytes(byte[] body) {
        // 0,4 int
        // 4,8 int
        // 8,12 int
    }
}

