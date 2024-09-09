package cn.byteswalk.eaglemqbroker.utils;

import cn.byteswalk.eaglemqbroker.cache.CommonCache;

import static cn.byteswalk.eaglemqbroker.constants.BrokerConstants.*;

/**
 *
 */
public class LogFileNameUtil {


    private static final int FULL_FILE_NAME_LENGTH = 8;
    private static final int ZERO = 0;
    private static final String ZERO_STR = "0";
    private static final String FIRST_FILE_NAME = "00000000";

    /**
     * 创建第一个commitLog文件名
     * @return 返回第一commitLog文件名
     */
    public static String createFirstFileName() {
        return FIRST_FILE_NAME;
    }

    /**
     * 根据老的文件名创建新的文件名
     * @param oldFileName 老的文件名
     * @return 返回新的文件名
     */
    public static String increaseCommitLogFileName(String oldFileName) {
        if (oldFileName.length() != FULL_FILE_NAME_LENGTH) {
            throw new IllegalArgumentException("fileName must has 8 chars");
        }
        Long fileIndex = Long.valueOf(oldFileName);
        fileIndex++;
        String newFileName = java.lang.String.valueOf(fileIndex);
        int newFileNameLength = newFileName.length();
        int needFullLength = FULL_FILE_NAME_LENGTH - newFileNameLength;
        if (needFullLength < ZERO) {
            throw new RuntimeException("unknown filename error");
        }
        StringBuilder stringBuffer = new StringBuilder();
        for (int i = ZERO; i < needFullLength; i++) {
            stringBuffer.append(ZERO_STR);
        }
        stringBuffer.append(newFileName);
        return stringBuffer.toString();
    }

    /**
     * 另一个根据老的文件名创建新的文件名
     * @param oldFileName 老的文件名
     * @return 返回新的文件名
     */
    public static String incrCommitLogFileName(String oldFileName) {
        if (oldFileName.length() != 8) {
            throw new IllegalArgumentException("fileName must have 8 characters.");
        }

        try {
            Long fileIndex = Long.valueOf(oldFileName);
            fileIndex++;
            // 使用格式化字符串，保证长度为8位，不足的前面补0
            return java.lang.String.format("%08d", fileIndex);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("fileName must be a numeric string.");
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred.", e);
        }
    }

    /**
     * 另一个根据老的文件名创建新的文件名
     * @param oldFileName 老的文件名
     * @return 返回新的文件名
     */
    public static String incrConsumeQueueFileName(String oldFileName) {
        return incrCommitLogFileName(oldFileName);
    }

    /**
     * 构建 CommitLog 文件路径
     * @param topicName topic 名称
     * @param commitLogFileName 新的topic文件名称
     * @return 返回新构建的 CommitLog 文件路径
     */
    public static String buildCommitLogFilePath(String topicName, String commitLogFileName) {
        return CommonCache.getGlobalProperties().getEagleMqHome()
                + BASE_COMMITLOG_PATH
                + topicName
                + SPLIT
                + commitLogFileName;
    }

    /**
     * 构建 ConsumeQueue 文件路径
     * @param topicName topic 名称
     * @param queueId ConsumeQueue Id
     * @param consumeQueueFileName 新的ConsumeQueue名称
     * @return 返回新构建的 ConsumeQueue 文件路径
     */
    public static String buildConsumeQueueFilePath(String topicName, Integer queueId, String consumeQueueFileName) {
        return CommonCache.getGlobalProperties().getEagleMqHome()
                + BASE_CONSUME_QUEUE_PATH
                + topicName
                + SPLIT
                + queueId
                + SPLIT
                + consumeQueueFileName;
    }
}
