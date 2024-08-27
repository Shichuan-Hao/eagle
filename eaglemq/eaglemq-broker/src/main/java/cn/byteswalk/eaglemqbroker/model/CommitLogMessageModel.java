package cn.byteswalk.eaglemqbroker.model;

import cn.byteswalk.eaglemqbroker.utils.ByteConvertUtils;

import java.util.Arrays;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-08-27 14:50
 * @Description: CommitLog真实数据存储对象模型
 * @Version: 1.0
 */
public class CommitLogMessageModel {


    /**
     * 消息的体积大小，单位是字节
     */
    private int size;

    /**
     * 真正的消息内容
     */
    private byte[] content;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "CommitLogMessageModel{" +
                "size=" + size +
                ", content=" + Arrays.toString(content) +
                '}';
    }

    public byte[] convertToBytes() {
        byte[] sizeByte = ByteConvertUtils.intToBytes(this.getSize());
        byte[] content = this.getContent();
        byte[] mergeResultByte = new byte[sizeByte.length + content.length];
        int j = 0;
        for (int i = 0; i < sizeByte.length; i++, j++) {
            mergeResultByte[j] = sizeByte[i];
        }
        for (int i = 0; i < content.length; i++, j++) {
            mergeResultByte[j] = content[i];
        }
        return mergeResultByte;
    }

}

