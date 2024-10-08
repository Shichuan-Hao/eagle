package com.byteswalk.eaglemq.broker.core;

import com.byteswalk.eaglemq.broker.model.CommitLogMessageModel;
import com.byteswalk.eaglemq.broker.cache.CommonCache;
import cn.byteswalk.eaglemq.common.constants.BrokerConstants;

import java.io.IOException;

/**
 * @Author idea
 * @Date: Created in 22:54 2024/3/25
 * @Description
 */
public class CommitLogAppendHandler {

    public void prepareMMapLoading(String topicName) throws IOException {
        CommitLogMMapFileModel mapFileModel = new CommitLogMMapFileModel();
        mapFileModel.loadFileInMMap(topicName,0, BrokerConstants.COMMIT_LOG_DEFAULT_MMAP_SIZE);
        CommonCache.getCommitLogMMapFileModelManager().put(topicName,mapFileModel);
    }

    public void appendMsg(String topic,byte[] content) throws IOException {
        CommitLogMMapFileModel mapFileModel = CommonCache.getCommitLogMMapFileModelManager().get(topic);
        if(mapFileModel==null) {
            throw new RuntimeException("topic is invalid!");
        }
        CommitLogMessageModel commitLogMessageModel = new CommitLogMessageModel();
        commitLogMessageModel.setContent(content);
        mapFileModel.writeContent(commitLogMessageModel,true);
    }

}
