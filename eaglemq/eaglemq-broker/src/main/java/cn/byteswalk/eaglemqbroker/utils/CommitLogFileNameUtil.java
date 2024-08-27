package cn.byteswalk.eaglemqbroker.utils;

/**
 *
 */
public class CommitLogFileNameUtil {


    /**
     * 创建第一个commitLog文件名
     * @return 返回第一commitLog文件名
     */
    public static String createFirstFileName() {
        return "00000000";
    }

    /**
     * 根据老的文件名创建新的文件名
     * @param oldFileName 老的文件名
     * @return 返回新的文件名
     */
    public static String increaseCommitLogFileName(String oldFileName) {
        if (oldFileName.length() != 8) {
            throw new IllegalArgumentException("fileName must has 8 chars");
        }
        Long fileIndex = Long.valueOf(oldFileName);
        fileIndex++;
        String newFileName = String.valueOf(fileIndex);
        int newFileNameLength = newFileName.length();
        int needFullLength = 8 - newFileNameLength;
        if (needFullLength < 0) {
            throw new RuntimeException("unknown filename error");
        }
        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < needFullLength; i++) {
            stringBuffer.append("0");
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
            return String.format("%08d", fileIndex);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("fileName must be a numeric string.");
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred.", e);
        }
    }

}
