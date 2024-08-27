package cn.byteswalk.eaglemqbroker.core;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * CommitLog 文件追加写处理类
 */
public class CommitLogAppendHandler {

    private final MMapFileModelManager mMapFileModelManager = new MMapFileModelManager();

    /**
     *
     * @param topicName the name of topic
     * @throws IOException the exception to io
     */
    public void prepareMMapLoading(String topicName)
            throws IOException {
        MMapFileModel mMapFileModel = new MMapFileModel();
        mMapFileModel.loadFileInMMap(topicName, 0, 1024 * 1024);
        mMapFileModelManager.put(topicName, mMapFileModel);
    }

    /**
     * 读消息
     * @param topicName 主题名称
     * @return 返回读取的消息内容
     */
    public String readMes(String topicName) {
        MMapFileModel mMapFileModel = mMapFileModelManager.get(topicName);
        checkMMapFileModelIsNull(mMapFileModel);
        byte[] readContent = mMapFileModel.readContent(0, 10);
        return new String(readContent);
    }

    /**
     * 像主题文件中追加写
     * @param topicName 主题名称
     * @param content 追加写的内容
     */
    public void appendMsg(String topicName, String content) {
        MMapFileModel mMapFileModel = mMapFileModelManager.get(topicName);
        checkMMapFileModelIsNull(mMapFileModel);
        mMapFileModel.writeContent(content.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 判断文件内存映射对象是否胃口，如果为空则抛出“Topic is invalid!”异常信息
     * @param mMapFileModel 文件内存映射对象
     */
    private void checkMMapFileModelIsNull(MMapFileModel mMapFileModel) {
        if (Objects.isNull(mMapFileModel)) {
            throw new RuntimeException("Topic is invalid!");
        }
    }

}
