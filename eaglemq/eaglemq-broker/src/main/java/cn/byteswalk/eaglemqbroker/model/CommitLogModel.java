package cn.byteswalk.eaglemqbroker.model;

/**
 * commitLog 文件的写入offset封装
 */
public class CommitLogModel {

    /**
     * 最新commitLog文件的名称
     */
    private String fileName;
    /**
     * 最新commitLog文件写入数据的地址
     */
    private Long offset;

    /**
     * commitLog 文件写入的最大上限体积
     *
     * 与 offset 比较，如果 offset 比 offsetList 小，则说明可能还可以写入 CommitLog 文件
     * diff = offsetLimit - offset
     *
     */
    private Long offsetLimit;


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getOffset() {
        return offset;
    }

    public void setOffset(Long offset) {
        this.offset = offset;
    }

    public Long getOffsetLimit() {
        return offsetLimit;
    }

    public void setOffsetLimit(Long offsetLimit) {
        this.offsetLimit = offsetLimit;
    }

    @Override
    public String toString() {
        return "CommitLogModel{" +
                "fileName='" + fileName + '\'' +
                ", offset='" + offset + '\'' +
                ", offsetLimit='" + offsetLimit + '\'' +
                '}';
    }
}
