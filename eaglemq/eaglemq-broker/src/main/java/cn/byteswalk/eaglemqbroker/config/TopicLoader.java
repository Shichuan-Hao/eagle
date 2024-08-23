package cn.byteswalk.eaglemqbroker.config;

import cn.byteswalk.eaglemqbroker.cache.CommonCache;
import cn.byteswalk.eaglemqbroker.model.TopicModel;
import cn.byteswalk.eaglemqbroker.utils.FileContentReaderUtils;
import com.alibaba.fastjson2.JSON;
import io.netty.util.internal.StringUtil;

import java.util.List;

public class TopicLoader {


    public void loadProperties() {
        GlobalProperties globalProperties = CommonCache.getGlobalProperties();
        String basePath = globalProperties.getEagleMqHome();
        if (StringUtil.isNullOrEmpty(basePath)) {
            throw new IllegalArgumentException("EAGLE_MQ_HOME is invalid!");
        }
        String topicJsonFilePath = basePath + "/broker/config/eaglemq-topic.json";
        String fileContent = FileContentReaderUtils.readFromFile(topicJsonFilePath);
        List<TopicModel> topicModelList = JSON.parseArray(fileContent, TopicModel.class);
        CommonCache.setTopicInfo(topicModelList);
    }
}
