package cn.byteswalk.eaglemq.broker.config;

import cn.byteswalk.eaglemq.broker.cache.CommonCache;
import cn.byteswalk.eaglemq.broker.model.TopicModel;
import cn.byteswalk.eaglemq.broker.utils.FileContentUtil;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson2.JSON;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static cn.byteswalk.eaglemq.broker.constants.BrokerConstants.COMMITLOG_CONFIG;
import static cn.byteswalk.eaglemq.broker.constants.BrokerConstants.DEFAULT_REFRESH_MQ_TOPIC_TIME_STEP;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-08-27 15:02
 * @Description: 负责将 mq 的主题配置信息加载到内存中
 * @Version: 1.0
 */
public class TopicLoader {


    private final Logger log = LoggerFactory.getLogger(TopicLoader.class);

    private String filePath;

    public void loadProperties() {
        GlobalProperties globalProperties = CommonCache.getGlobalProperties();
        String basePath = globalProperties.getEagleMqHome();

        Objects.requireNonNull(basePath, "EAGLE_MQ_HOME is invalid!");

        filePath = basePath + COMMITLOG_CONFIG;
        String fileContent = FileContentUtil.readFromFile(filePath);
        List<TopicModel> topicModelList = JSON.parseArray(fileContent, TopicModel.class);

        CommonCache.setTopicModelList(topicModelList);
    }

    /**
     * 开启一个刷新内存到磁盘的认为
     */
    public void startRefreshTopicInfoTask() {
        // 异步线程
        // 每隔 n秒（可以加入配置文件中）将内存中的配置刷新到磁盘里面（mmap 对小文件）
        // 类似 Redis rdb 持久化思路（并发）
        CommonThreadPoolConfig.refreshTopicExecutor.execute(new Runnable() {
            @Override
            @SuppressWarnings("InfiniteLoopStatement")
            public void run() {
                do {
                    try {
                        TimeUnit.SECONDS.sleep(DEFAULT_REFRESH_MQ_TOPIC_TIME_STEP);
                        List<TopicModel> topicModelList = CommonCache.getTopicModelList();
                        String prettyFormat = String.valueOf(SerializerFeature.PrettyFormat);
                        String topicModelJSONArray = JSON.toJSONString(topicModelList, prettyFormat);
                        FileContentUtil.overWriteToFile(filePath, topicModelJSONArray);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } while (true);
            }
        });

    }
}
