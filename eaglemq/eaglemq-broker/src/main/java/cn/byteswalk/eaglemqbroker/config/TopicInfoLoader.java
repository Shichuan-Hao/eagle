package cn.byteswalk.eaglemqbroker.config;

import cn.byteswalk.eaglemqbroker.cache.CommonCache;
import io.netty.util.internal.StringUtil;

public class TopicInfoLoader {

    private TopicInfo topicInfo;

    public void loadProperties() {
        GlobalProperties globalProperties = CommonCache.getGlobalProperties();
        String basePath = globalProperties.getEagleMqHome();
        if (StringUtil.isNullOrEmpty(basePath)) {
            throw new IllegalArgumentException("EAGLE_MQ_HOME is invalid!");
        }
        String topicJsonFilePath = basePath + "/broker/config/eaglemq-topic.json";
        topicInfo = new TopicInfo();
    }
}
