package cn.byteswalk.eaglemq.broker.core;

import cn.byteswalk.eaglemq.broker.cache.CommonCache;
import cn.byteswalk.eaglemq.broker.constants.BrokerConstants;
import cn.byteswalk.eaglemq.broker.model.CommitLogMessageModel;

import java.io.IOException;
import java.util.Objects;

/**
 * CommitLog 文件追加写数据处理类
 */
public class CommitLogAppendHandler {

    /**
     *
     * @param topicName 消息主题名称
     * @throws IOException the exception to io
     */
    public void prepareMMapLoading(String topicName)
            throws IOException {
        CommitLogMMapFileModel commitLogMMapFileModel = new CommitLogMMapFileModel();
        commitLogMMapFileModel.loadFileInMMap(topicName, BrokerConstants.START_OFFSET, BrokerConstants.COMMIT_DEFAULT_MMAP_SIZE);
        CommonCache.getCommitLogMMapFileModelManager().put(topicName, commitLogMMapFileModel);
    }


    /**
     * 像主题文件中追加写
     * @param topicName 主题名称
     * @param content 追加写的内容
     */
    public void appendMsg(String topicName, byte[] content)
            throws IOException {
        CommitLogMMapFileModelManager commitLogMMapFileModelManager = CommonCache.getCommitLogMMapFileModelManager();
        CommitLogMMapFileModel commitLogMMapFileModel = commitLogMMapFileModelManager.get(topicName);

        Objects.requireNonNull(commitLogMMapFileModelManager, "The name of topic is invalid!");

        CommitLogMessageModel commitLogMessageModel = new CommitLogMessageModel();
        commitLogMessageModel.setContent(content);
        commitLogMMapFileModel.writeContent(commitLogMessageModel);
    }

}
