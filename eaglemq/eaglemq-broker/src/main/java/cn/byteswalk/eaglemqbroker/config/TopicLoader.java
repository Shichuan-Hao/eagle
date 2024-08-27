package cn.byteswalk.eaglemqbroker.config;

import cn.byteswalk.eaglemqbroker.cache.CommonCache;
import cn.byteswalk.eaglemqbroker.model.TopicModel;
import cn.byteswalk.eaglemqbroker.utils.FileContentUtil;
import com.alibaba.fastjson2.JSON;
import io.netty.util.internal.StringUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static cn.byteswalk.eaglemqbroker.constants.BrokerConstants.DEFAULT_REFRESH_MQ_TOPIC_TIME_STEP;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-08-27 15:02
 * @Description: 负责将 mq 的主题配置信息加载到内存中
 * @Version: 1.0
 */
public class TopicLoader {


    private String topicJsonFilePath;

    public void loadProperties() {
        GlobalProperties globalProperties = CommonCache.getGlobalProperties();
        String basePath = globalProperties.getEagleMqHome();
        if (StringUtil.isNullOrEmpty(basePath)) {
            throw new IllegalArgumentException("EAGLE_MQ_HOME is invalid!");
        }

        topicJsonFilePath = basePath + "/broker/config/eaglemq-topic.json";
        String fileContent = FileContentUtil.readFromFile(topicJsonFilePath);
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
            public void run() {
                do {
                    try {
                        TimeUnit.SECONDS.sleep(DEFAULT_REFRESH_MQ_TOPIC_TIME_STEP);
                        List<TopicModel> topicModelList = CommonCache.getTopicModelList();
                        FileContentUtil.overWriteToFile(topicJsonFilePath, JSON.toJSONString(topicModelList));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } while (true);
            }
        });

    }
}
