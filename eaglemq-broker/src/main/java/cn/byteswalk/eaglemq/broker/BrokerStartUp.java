package cn.byteswalk.eaglemq.broker;

import cn.byteswalk.eaglemq.broker.cache.CommonCache;
import cn.byteswalk.eaglemq.broker.config.ConsumeQueueOffsetLoader;
import cn.byteswalk.eaglemq.broker.config.GlobalPropertiesLoader;
import cn.byteswalk.eaglemq.broker.config.TopicLoader;
import cn.byteswalk.eaglemq.broker.core.CommitLogAppendHandler;
import cn.byteswalk.eaglemq.broker.core.ConsumeQueueAppendHandler;
import cn.byteswalk.eaglemq.broker.core.ConsumeQueueConsumeHandler;
import cn.byteswalk.eaglemq.broker.model.TopicModel;
import cn.byteswalk.eaglemq.broker.netty.nameserver.NameServerClient;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class BrokerStartUp {

//    private static final Logger logger = LoggerFactory.getLogger(BrokerStartUp.class);

    private static GlobalPropertiesLoader globalPropertiesLoader;
    private static TopicLoader topicLoader;
    private static CommitLogAppendHandler commitLogAppendHandler;
    private static ConsumeQueueOffsetLoader consumeQueueOffsetLoader;
    private static ConsumeQueueAppendHandler consumeQueueAppendHandler;
    private static ConsumeQueueConsumeHandler consumeQueueConsumeHandler;

    /**
     * 初始化配置
     */
    private static void initProperties()
            throws IOException {
        globalPropertiesLoader      = new GlobalPropertiesLoader();
        topicLoader                 = new TopicLoader();
        consumeQueueOffsetLoader    = new ConsumeQueueOffsetLoader();
        commitLogAppendHandler      = new CommitLogAppendHandler();
        consumeQueueConsumeHandler  = new ConsumeQueueConsumeHandler();
        consumeQueueAppendHandler   = new ConsumeQueueAppendHandler();

        // 加载全局属性
        globalPropertiesLoader.loadProperties();

        // 创建 [topic]CommitLog 属性加载器并载入相关 topic 属性和刷新磁盘
        topicLoader.loadProperties();
        topicLoader.startRefreshTopicInfoTask();

        // 创建 ConsumeQueue 属性加载器并载入相关属性和刷新磁盘
        consumeQueueOffsetLoader.loadProperties();
        consumeQueueOffsetLoader.startRefreshConsumeQueueOffsetTask();

        // CommitLog MMap 映射预加载
        for (TopicModel topicModel : CommonCache.getTopicModelMap().values()) {
            String topicName = topicModel.getTopicName();
            // 预加载（映射）
            commitLogAppendHandler.prepareMMapLoading(topicName);
            consumeQueueAppendHandler.prepareMMapLoading(topicName);
        }

    }

    /**
     * 初始化和nameserver的长连接通道
     */
    private static void initNameServerChannel() {
       CommonCache.getNameServerClient().initConnection();
       CommonCache.getNameServerClient().sendRegistry();
    }

    public static void main(String[] args)
            throws IOException {
        // 先将配置加载好
        initProperties();
        // 与注册中心 Nameserver 创建长连接
        initNameServerChannel();
    }
}
