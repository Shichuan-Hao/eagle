package cn.byteswalk.eaglemqbroker.core;

import cn.byteswalk.eaglemqbroker.cache.CommonCache;
import cn.byteswalk.eaglemqbroker.model.QueueModel;
import cn.byteswalk.eaglemqbroker.model.TopicModel;
import cn.byteswalk.eaglemqbroker.utils.LogFileNameUtil;
import cn.byteswalk.eaglemqbroker.utils.PutMessageLock;
import cn.byteswalk.eaglemqbroker.utils.UnfairReentrantLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Objects;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-04 10:37
 * @Description: 对 ConsumeQueue 文件做 mmap 映射的核心对象
 * @Version: 1.0
 */
public class ConsumeQueueMMapFileModel {

    private static final String RW_ACCESS_MODE = "rw";

    private File file;
    private MappedByteBuffer mappedByteBuffer;
    private ByteBuffer readBuffer;
    private FileChannel fileChannel;
    private String topicName;
    private Integer queueId;
    private String consumeQueueFileName;
    private PutMessageLock putMessageLock;

    private static final Logger logger = LoggerFactory.getLogger(ConsumeQueueMMapFileModel.class);

    private final int CONSUME_QUEUE_EACH_MSG_SIZE = 12;

    /**
     * 指定offset做文件映射
     *
     * @param topicName   消息主题
     * @param startOffset 开始映射的offset
     * @param mappedSize  映射的体积（endOffset-startOffset/byte）
     */
    public void loadFileInMMap(
            String topicName,
            Integer queueId,
            int startOffset,
            int mappedSize)
            throws IOException {

        this.topicName = topicName;
        this.queueId = queueId;
        // 获取最新的 ConsumerQueueFile
        String filePath = this.getLatestConsumeQueueFile();
        this.doMMap(filePath, startOffset, mappedSize);
        putMessageLock = new UnfairReentrantLock();
    }

    private void doMMap(String filePath, int startOffset, int mappedSize)
            throws IOException {
        file = new File(filePath);
        if (!file.exists() || !file.canWrite()) {
            throw new IOException("File path is invalid or not writable: " + filePath);
        }

        this.fileChannel = new RandomAccessFile(file, RW_ACCESS_MODE).getChannel();
        this.mappedByteBuffer = this.fileChannel.map(FileChannel.MapMode.READ_WRITE, startOffset, mappedSize);
        this.readBuffer = this.mappedByteBuffer.slice();
    }

    public void writeContent(byte[] content) {
        this.writeContent(content, false);
    }

    public void writeContent(byte[] content, boolean force) {
        try {
            putMessageLock.lock();
            mappedByteBuffer.put(content);
            if (force) {
                mappedByteBuffer.force();
            }
        } finally {
            putMessageLock.unlock();
        }
    }

    private String getLatestConsumeQueueFile() {
        TopicModel topicModel = CommonCache.getTopicModelMap().get(topicName);
        if (Objects.isNull(topicModel)) {
            throw new IllegalArgumentException("topic is inValid! topicName is " + topicName);
        }
        List<QueueModel> queueModels = topicModel.getQueueModels();
        QueueModel queueModel = queueModels.get(queueId);
        if (Objects.isNull(queueModel)) {
            throw new IllegalArgumentException("topic is inValid! topicName is " + topicName);
        }
        int diff = queueModel.countDiff();
        String filePath = null;
        if (diff == 0) {
            // 已经写满了
            ConsumeQueueFilePath newConsumeQueueFile = this.createNewConsumeQueueFile(queueModel.getFileName());
            filePath = newConsumeQueueFile.getFilePath();
        } else if (diff > 0) {
            // 还有机会写入
            filePath = LogFileNameUtil.buildConsumeQueueFilePath(topicName, queueId, queueModel.getFileName());
        }
        return filePath;
    }

    private ConsumeQueueFilePath createNewConsumeQueueFile(String fileName) {
        String newFileName = LogFileNameUtil.incrConsumeQueueFileName(fileName);
        String newFilePath = LogFileNameUtil.buildConsumeQueueFilePath(topicName, queueId, newFileName);
        File newConsumeQueueFile = new File(newFilePath);
        try {
            if (newConsumeQueueFile.createNewFile()) {
                logger.info("创建了新的 ConsumeQueue 文件，文件名称：{}， 文件位置：{}", newFileName, newFilePath);
            } else {
                throw new RuntimeException("create the new ConsumeQueue file error!");
            }
        } catch (IOException e) {
            throw new RuntimeException("create the new ConsumeQueue file error!");
        }
        return new ConsumeQueueFilePath(newFileName, newFilePath);
    }

    public byte[] readContent(int pos) {
        int size = 12;
        // 理解为打开一个窗口  readBuffer pos:0~limit(开了一个窗口)
        // readBuf 任意修改
        ByteBuffer readBuf = readBuffer.slice();
        readBuf.position(pos);
        byte[] content = new byte[CONSUME_QUEUE_EACH_MSG_SIZE];
        readBuf.get(content);
        return content;
    }


    class ConsumeQueueFilePath {
        private String fileName;
        private String filePath;

        public ConsumeQueueFilePath(String fileName, String filePath) {
            this.fileName = fileName;
            this.filePath = filePath;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }
    }

    public Integer getQueueId() {
        return queueId;
    }

}

