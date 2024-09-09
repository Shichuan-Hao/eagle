package cn.byteswalk.eaglemqbroker;

import cn.byteswalk.eaglemqbroker.cache.CommonCache;
import cn.byteswalk.eaglemqbroker.config.ConsumeQueueOffsetLoader;
import cn.byteswalk.eaglemqbroker.config.GlobalPropertiesLoader;
import cn.byteswalk.eaglemqbroker.config.TopicLoader;
import cn.byteswalk.eaglemqbroker.core.CommitLogAppendHandler;
import cn.byteswalk.eaglemqbroker.core.ConsumeQueueAppendHandler;
import cn.byteswalk.eaglemqbroker.core.ConsumeQueueConsumeHandler;
import cn.byteswalk.eaglemqbroker.model.TopicModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class BrokerStartUp {

    private static final Logger logger = LoggerFactory.getLogger(BrokerStartUp.class);

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
        globalPropertiesLoader = new GlobalPropertiesLoader();
        topicLoader = new TopicLoader();
        consumeQueueOffsetLoader = new ConsumeQueueOffsetLoader();
        commitLogAppendHandler = new CommitLogAppendHandler();
        consumeQueueConsumeHandler = new ConsumeQueueConsumeHandler();
        consumeQueueAppendHandler = new ConsumeQueueAppendHandler();

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


    public static void main(String[] args)
            throws IOException, InterruptedException {
        //加载配置 ，缓存对象的生成
        initProperties();
        //模拟初始化文件映射
        String topic = "order_cancel_topic";
        String userServiceConsumeGroup = "user_service_group";
        String orderServiceConsumeGroup = "order_service_group";
        new Thread(() -> {
            while (true) {
                byte[] content = consumeQueueConsumeHandler.consume(topic, userServiceConsumeGroup, 0);
                if (content != null && content.length != 0) {
                    System.out.println(userServiceConsumeGroup + ",消费内容:" + new String(content));
                    consumeQueueConsumeHandler.ack(topic, userServiceConsumeGroup, 0);
                } else {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();

        new Thread(() -> {
            while (true) {
                byte[] content = consumeQueueConsumeHandler.consume(topic, orderServiceConsumeGroup, 0);
                if (content != null) {
                    System.out.println(orderServiceConsumeGroup + ",消费内容:" + new String(content));
                    consumeQueueConsumeHandler.ack(topic, orderServiceConsumeGroup, 0);
                } else {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();

        AtomicInteger i = new AtomicInteger();
        new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            while (true) {
                try {
                    commitLogAppendHandler.appendMsg(topic, ("message_" + (i.getAndIncrement())).getBytes());
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        logger.info("\33[32;4m开始多线程消费验证\33[0;4m");
    }
}
