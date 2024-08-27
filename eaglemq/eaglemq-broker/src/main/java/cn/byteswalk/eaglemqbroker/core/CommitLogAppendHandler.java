package cn.byteswalk.eaglemqbroker.core;


import cn.byteswalk.eaglemqbroker.constants.BrokerConstants;
import cn.byteswalk.eaglemqbroker.model.CommitLogMessageModel;

import java.io.IOException;
import java.util.Objects;

/**
 * CommitLog 文件追加写数据处理类
 */
public class CommitLogAppendHandler {

    /**
     * MMapFileModelManager
     */
    private final MMapFileModelManager mMapFileModelManager = new MMapFileModelManager();

    /**
     *
     * @param topicName 消息主题名称
     * @throws IOException the exception to io
     */
    public void prepareMMapLoading(String topicName)
            throws IOException {
        MMapFileModel mMapFileModel = new MMapFileModel();
        mMapFileModel.loadFileInMMap(topicName, 0, BrokerConstants.COMMIT_DEFAULT_MMAP_SIZE);
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
    public void appendMsg(String topicName, byte[] content)
            throws IOException {
        MMapFileModel mMapFileModel = mMapFileModelManager.get(topicName);

        checkMMapFileModelIsNull(mMapFileModel);

        CommitLogMessageModel commitLogMessageModel = new CommitLogMessageModel();
        commitLogMessageModel.setSize(content.length);
        commitLogMessageModel.setContent(content);
        mMapFileModel.writeContent(commitLogMessageModel);
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
