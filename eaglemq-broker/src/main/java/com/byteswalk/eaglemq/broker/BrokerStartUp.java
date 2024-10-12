package com.byteswalk.eaglemq.broker;

import com.byteswalk.eaglemq.broker.cache.CommonCache;
import com.byteswalk.eaglemq.broker.core.CommitLogAppendHandler;
import com.byteswalk.eaglemq.broker.core.ConsumeQueueAppendHandler;
import com.byteswalk.eaglemq.broker.core.ConsumeQueueConsumeHandler;
import com.byteswalk.eaglemq.broker.model.EagleMqTopicModel;
import com.byteswalk.eaglemq.broker.config.ConsumeQueueOffsetLoader;
import com.byteswalk.eaglemq.broker.config.EagleMqTopicLoader;
import com.byteswalk.eaglemq.broker.config.GlobalPropertiesLoader;

import java.io.IOException;

/**
 * @Author idea
 * @Date: Created in 22:57 2024/3/26
 * @Description
 */
public class BrokerStartUp {

    private static GlobalPropertiesLoader globalPropertiesLoader;
    private static EagleMqTopicLoader eagleMqTopicLoader;
    private static CommitLogAppendHandler commitLogAppendHandler;
    private static ConsumeQueueOffsetLoader consumeQueueOffsetLoader;
    private static ConsumeQueueAppendHandler consumeQueueAppendHandler;
    private static ConsumeQueueConsumeHandler consumeQueueConsumeHandler;

    /**
     * 初始化配置逻辑
     */
    private static void initProperties() throws IOException {
        globalPropertiesLoader     = new GlobalPropertiesLoader();
        eagleMqTopicLoader         = new EagleMqTopicLoader();
        consumeQueueOffsetLoader   = new ConsumeQueueOffsetLoader();
        consumeQueueConsumeHandler = new ConsumeQueueConsumeHandler();
        commitLogAppendHandler     = new CommitLogAppendHandler();
        consumeQueueAppendHandler  = new ConsumeQueueAppendHandler();

        globalPropertiesLoader.loadProperties();
        eagleMqTopicLoader.loadProperties();
        eagleMqTopicLoader.startRefreshEagleMqTopicInfoTask();
        consumeQueueOffsetLoader.loadProperties();
        consumeQueueOffsetLoader.startRefreshConsumeQueueOffsetTask();

        for (EagleMqTopicModel eagleMqTopicModel : CommonCache.getEagleMqTopicModelMap().values()) {
            String topicName = eagleMqTopicModel.getTopic();
            commitLogAppendHandler.prepareMMapLoading(topicName);
            consumeQueueAppendHandler.prepareConsumeQueue(topicName);
        }
    }

    /**
     * 初始化和nameserver的长链接通道
     */
    private static void initNameServerChannel() {
        CommonCache.getNameServerClient().initConnection();
        CommonCache.getNameServerClient().sendRegistryMsg();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        //加载配置 ，缓存对象的生成
        initProperties();
        initNameServerChannel();
        Thread.sleep(10000);
    }
}
