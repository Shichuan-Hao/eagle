package cn.byteswalk.eaglemqbroker.model;

import java.util.Arrays;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-08-27 14:50
 * @Description: CommitLog真实数据存储对象模型
 * @Version: 1.0
 */
public class CommitLogMessageModel {

    /**
     * 真正的消息内容
     */
    private byte[] content;

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "CommitLogMessageModel{" +
                ", content=" + Arrays.toString(content) +
                '}';
    }

    public byte[] convertToBytes() {
        return getContent();
    }

}

