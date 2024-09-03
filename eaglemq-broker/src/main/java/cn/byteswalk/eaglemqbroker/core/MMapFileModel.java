package cn.byteswalk.eaglemqbroker.core;

import cn.byteswalk.eaglemqbroker.cache.CommonCache;
import cn.byteswalk.eaglemqbroker.constants.BrokerConstants;
import cn.byteswalk.eaglemqbroker.model.CommitLogMessageModel;
import cn.byteswalk.eaglemqbroker.model.CommitLogModel;
import cn.byteswalk.eaglemqbroker.model.ConsumerQueueDetailModel;
import cn.byteswalk.eaglemqbroker.model.TopicModel;
import cn.byteswalk.eaglemqbroker.utils.CommitLogFileNameUtil;
import cn.byteswalk.eaglemqbroker.utils.PutMessageLock;
import cn.byteswalk.eaglemqbroker.utils.UnfairReentrantLock;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import java.security.AccessController;
import java.security.PrivilegedAction;

import java.time.chrono.MinguoDate;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-08-27 15:02
 * @Description: 最基础的 mmap 对象模型
 * @Version: 1.0
 */
public class MMapFileModel {

    private static final String RW_ACCESS_MODE = "rw";

    private File file;
    private MappedByteBuffer mappedByteBuffer;
    private FileChannel fileChannel;
    private String topicName;
    private PutMessageLock putMessageLock;

    /**
     * 指定offset做文件映射
     *
     * @param topicName   消息主题
     * @param startOffset 开始映射的offset
     * @param mappedSize  映射的体积（endOffset-startOffset/byte）
     */
    public void loadFileInMMap(String topicName, int startOffset, int mappedSize)
            throws IOException {
        // 获取最新的 commitLog 文件
        this.topicName = topicName;
        String filePath = getLatestCommitLogFile(topicName);
        this.doMMap(filePath, startOffset, mappedSize);
        // 默认非公平模式
        putMessageLock = new UnfairReentrantLock();
    }

    /**
     * 支持从文件从指定的 offset 开始读取内容
     *
     * @param readOffset 读取内容的起始位置
     * @param size       读取内容的大小
     * @return 返回读取内容
     */
    public byte[] readContent(int readOffset, int size) {
        // consumerQueue
        mappedByteBuffer.position(readOffset);
        byte[] content = new byte[size];
        int j = 0;
        for (int i = 0; i < size; i++) {
            // 从内存里访问，快速高效，不用担心
            byte b = mappedByteBuffer.get(readOffset + i);
            content[j++] = b;
        }
        return content;
    }

    /**
     * 更高性能的一种写入api
     *
     * @param commitLogMessageModel 内容
     */
    public void writeContent(CommitLogMessageModel commitLogMessageModel)
            throws IOException {
        this.writeContent(commitLogMessageModel, false);
    }

    /**
     * 写入数据到磁盘中
     *
     * @param commitLogMessageModel 内容
     * @param force   强制刷盘标志
     */
    public void writeContent(CommitLogMessageModel commitLogMessageModel, boolean force)
            throws IOException {
        //定位到最新的commitLog文件中，记录下当前文件是否已经写满，如果写满，则创建新的文件，并且做新的mmap映射 done!
        //如果当前文件没有写满，对content内容做一层封装，done!
        // 再判断写入是否会导致commitLog写满，如果不会，则选择当前commitLog，如果会则创建新文件，并且做mmap映射 done!
        //定位到最新的commitLog文件之后，写入 done!
        //定义一个对象专门管理各个topic的最新写入offset值，并且定时刷新到磁盘中（mmap？）
        //写入数据，offset变更，如果是高并发场景，offset是不是会被多个线程访问？

        //offset会用一个原子类AtomicLong去管理
        //线程安全问题：线程1：111，线程2：122
        //加锁机制 （锁的选择非常重要）
        TopicModel topicModel = CommonCache.getTopicModelMap().get(topicName);
        if (topicModel == null) {
            throw new IllegalArgumentException("topicModel is null");
        }
        CommitLogModel commitLogModel = topicModel.getCommitLogModel();
        if (commitLogModel == null) {
            throw new IllegalArgumentException("commitLogModel is null");
        }


        // 默认刷到 page cache 中，如果需要强制刷盘，这里要兼容
        // 线程安全保证：上锁
        putMessageLock.lock();
        // 检测当前 CommitLog 是否还有足够空间写入
        this.checkCommitLogHasEnableSpace(commitLogMessageModel);
        byte[] writeContent = commitLogMessageModel.convertToBytes();
        mappedByteBuffer.put(writeContent);
        AtomicInteger currentLatestMsgOffset  = commitLogModel.getOffset();
        int writeContentLength = writeContent.length;
        this.dispatcher(writeContentLength, currentLatestMsgOffset.get());
        currentLatestMsgOffset.addAndGet(writeContentLength);
        if (force) {
            // 强制刷盘
            mappedByteBuffer.force();
        }
        putMessageLock.unlock();
    }

    /**
     * 将 ConsumerQueue 写入
     * @param msgLength 消息的长度
     * @param msgIndex 消息的起始位置
     */
    private void dispatcher(int msgLength, int msgIndex) {
        TopicModel topicModel = CommonCache.getTopicModelMap().get(topicName);
        if (topicModel == null) {
            throw new RuntimeException("topic is undefined");
        }
        ConsumerQueueDetailModel consumerQueueDetailModel = new ConsumerQueueDetailModel();
        consumerQueueDetailModel.setCommitLogFileName(topicModel.getCommitLogModel().getFileName());
        consumerQueueDetailModel.setMsgIndex(msgIndex);
        consumerQueueDetailModel.setMsgLength(msgLength);
    }

    /**
     * 释放mmap内存
     */
    public void clean() {
        if (mappedByteBuffer != null && mappedByteBuffer.isDirect() && mappedByteBuffer.capacity() != 0) {
            // 执行需要的操作
            invoke(invoke(viewed(mappedByteBuffer), "cleaner"), "clean");
        }
        // Ensure closing file channel
        if (fileChannel != null && fileChannel.isOpen()) {
            try {
                fileChannel.close();
            } catch (IOException e) {
                // Handle exception
                throw new RuntimeException(e);
            }
        }
    }

    private void doMMap(String filePath, int startOffset, int mappedSize) throws IOException {
        file = new File(filePath);
        if (!file.exists() || !file.canWrite()) {
            throw new IOException("File path is invalid or not writable: " + filePath);
        }

        this.fileChannel = new RandomAccessFile(file, RW_ACCESS_MODE).getChannel();
        this.mappedByteBuffer = this.fileChannel.map(FileChannel.MapMode.READ_WRITE, startOffset, mappedSize);
    }

    /**
     * 获取 commitLog 最新的地址
     * @param topicName 消息主题
     * @return 返回 commitLog 文件最新的地址
     */
    private String getLatestCommitLogFile(String topicName) {
        TopicModel topicModel = CommonCache.getTopicModelMap().get(topicName);
        if (Objects.isNull(topicModel)) {
            throw new IllegalArgumentException("topic is inValid! topicName is " + topicName);
        }
        CommitLogModel commitLogModel = topicModel.getCommitLogModel();
        // 剩余可写入的空间值
        long diff = commitLogModel.countDiff();
        String filePath = null;
        if (diff == 0) {
            // 已经写满了
            CommitLogFilePath newCommitLogFile = this.createNewCommitLogFile(topicName, commitLogModel);
            filePath = newCommitLogFile.getFilePath();
        }  else if (diff > 0) {
            // 还有机会写入
            filePath = CommitLogFileNameUtil.buildCommitLogFilePath(topicName, commitLogModel.getFileName());;
        }
        return filePath;
    }

    /**
     * 获取最新的 commitLog 文件路径
     * @param topicName 消息主题
     * @param commitLogModel commitLog 对象
     * @return 返回最新的 commitLog 文件路径
     */
    private CommitLogFilePath createNewCommitLogFile(String topicName, CommitLogModel commitLogModel) {
        // commitLog 命名规范
        String newFileName = CommitLogFileNameUtil.incrCommitLogFileName(commitLogModel.getFileName());
        String newFilePath = CommitLogFileNameUtil.buildCommitLogFilePath(topicName, newFileName);
        File newCommmitLogFile = new File(newFilePath);
        try {
            // 新的 commitLog文件创建
            if (newCommmitLogFile.createNewFile()) {
                System.out.println("创建了新的 CommitLog 文件，文件名称 :" + newFileName + "\n 文件位置：" + newFilePath);
            } else {
                throw new RuntimeException("create the new CommitLog File error");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new CommitLogFilePath(newFileName, newFilePath);
    }

    private void checkCommitLogHasEnableSpace(CommitLogMessageModel commitLogMessageModel)
            throws IOException {
        TopicModel topicModel = CommonCache.getTopicModelMap().get(this.topicName);
        if (Objects.isNull(topicModel)) {
            throw new IllegalArgumentException("topic is inValid! topicName is " + topicName);
        }
        CommitLogModel commitLogModel = topicModel.getCommitLogModel();
        // 剩余可写入的空间大小
        long writeAbleOffsetNum = commitLogModel.countDiff();
        // 空间不足，需要创建新的commitLog文件并且做映射
        if (writeAbleOffsetNum < commitLogMessageModel.getSize()) {
            // 00000000 文件 -> 00000001 文件
            // 空间利用率不是100%，比如某个commitLog剩余150byte【碎片空间】大小的空间，最新的消息体积是151byte【可以通过程序过滤掉】
            CommitLogFilePath commitLogFilePath = this.createNewCommitLogFile(this.topicName, commitLogModel);
            commitLogModel.setFileName(commitLogFilePath.getFileName());
            commitLogModel.setOffsetLimit(Long.valueOf(BrokerConstants.COMMIT_DEFAULT_MMAP_SIZE));
            commitLogModel.setOffset(new AtomicInteger(0));

            // 新文件路径映射进来
            this.doMMap(commitLogFilePath.getFilePath(), 0, BrokerConstants.COMMIT_DEFAULT_MMAP_SIZE);
        }
    }


    // AccessController.doPrivileged 在 jdk17 已经过时
    private Object invoke(final Object target, final String methodName, final Class<?>... args) {
        return AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
            try {
                Method method = method(target, methodName, args);
                method.setAccessible(true);
                return method.invoke(target);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private Method method(Object target, String methodName, Class<?>[] args)
            throws NoSuchMethodException {
        try {
            return target.getClass().getMethod(methodName, args);
        } catch (NoSuchMethodException e) {
            return target.getClass().getDeclaredMethod(methodName, args);
        }
    }

    private ByteBuffer viewed(ByteBuffer buffer) {
        String methodName = "viewedBuffer";
        Method[] methods = buffer.getClass().getMethods();
        for (Method method : methods) {
            if (method.getName().equals("attachment")) {
                methodName = "attachment";
                break;
            }
        }

        ByteBuffer viewedBuffer = (ByteBuffer) invoke(buffer, methodName);
        if (viewedBuffer == null) {
            return buffer;
        } else {
            return viewed(viewedBuffer);
        }
    }


    /**
     * CommitLogFilePath
     */
    class CommitLogFilePath {

        private String fileName;
        private String filePath;

        public CommitLogFilePath(String fileName, String filePath) {
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

}
