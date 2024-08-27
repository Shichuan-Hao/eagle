package cn.byteswalk.eaglemqbroker.config;

import cn.byteswalk.eaglemqbroker.cache.CommonCache;
import cn.byteswalk.eaglemqbroker.model.TopicModel;
import cn.byteswalk.eaglemqbroker.utils.FileContentReaderUtil;
import com.alibaba.fastjson2.JSON;
import io.netty.util.internal.StringUtil;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TopicLoader {


    public void loadProperties() {
        GlobalProperties globalProperties = CommonCache.getGlobalProperties();
        String basePath = globalProperties.getEagleMqHome();
        if (StringUtil.isNullOrEmpty(basePath)) {
            throw new IllegalArgumentException("EAGLE_MQ_HOME is invalid!");
        }

        String topicJsonFilePath = basePath + "/broker/config/eaglemq-topic.json";
        String fileContent = FileContentReaderUtil.readFromFile(topicJsonFilePath);
        List<TopicModel> topicModelList = JSON.parseArray(fileContent, TopicModel.class);
        Map<String, TopicModel> topicModelMap = topicModelList.stream()
                .collect(Collectors.toMap(TopicModel::getTopicName, item -> item));
        CommonCache.setTopicModelMap(topicModelMap);
    }
}
