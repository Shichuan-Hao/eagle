package cn.byteswalk.eaglemqbroker.core;

import cn.byteswalk.eaglemqbroker.model.CommitLogMessageModel;

import java.io.IOException;
import java.util.Objects;

import static cn.byteswalk.eaglemqbroker.constants.BrokerConstants.COMMIT_DEFAULT_MMAP_SIZE;
import static cn.byteswalk.eaglemqbroker.constants.BrokerConstants.START_OFFSET;

/**
 * CommitLog 文件追加写数据处理类
 */
public class CommitLogAppendHandler {

    /**
     * MMapFileModelManager
     */
    private final CommitLogMMapFileModelManager commitLogMMapFileModelManager = new CommitLogMMapFileModelManager();

    /**
     *
     * @param topicName 消息主题名称
     * @throws IOException the exception to io
     */
    public void prepareMMapLoading(String topicName)
            throws IOException {
        CommitLogMMapFileModel commitLogMMapFileModel = new CommitLogMMapFileModel();
        commitLogMMapFileModel.loadFileInMMap(topicName, START_OFFSET, COMMIT_DEFAULT_MMAP_SIZE);
        commitLogMMapFileModelManager.put(topicName, commitLogMMapFileModel);
    }


    /**
     * 像主题文件中追加写
     * @param topicName 主题名称
     * @param content 追加写的内容
     */
    public void appendMsg(String topicName, byte[] content)
            throws IOException {
        CommitLogMMapFileModel commitLogMMapFileModel = commitLogMMapFileModelManager.get(topicName);

        checkMMapFileModelIsNull(commitLogMMapFileModel);

        CommitLogMessageModel commitLogMessageModel = new CommitLogMessageModel();
        commitLogMessageModel.setContent(content);
        commitLogMMapFileModel.writeContent(commitLogMessageModel);
    }

    /**
     * 判断文件内存映射对象是否胃口，如果为空则抛出“Topic is invalid!”异常信息
     * @param commitLogMMapFileModel 文件内存映射对象
     */
    private void checkMMapFileModelIsNull(CommitLogMMapFileModel commitLogMMapFileModel) {
        if (Objects.isNull(commitLogMMapFileModel)) {
            throw new RuntimeException("Topic is invalid!");
        }
    }


}
