package cn.byteswalk.eaglemq.broker;

import cn.byteswalk.eaglemq.broker.cache.CommonCache;
import cn.byteswalk.eaglemq.broker.config.ConsumeQueueOffsetLoader;
import cn.byteswalk.eaglemq.broker.config.GlobalPropertiesLoader;
import cn.byteswalk.eaglemq.broker.config.TopicLoader;
import cn.byteswalk.eaglemq.broker.core.CommitLogAppendHandler;
import cn.byteswalk.eaglemq.broker.core.ConsumeQueueAppendHandler;
import cn.byteswalk.eaglemq.broker.core.ConsumeQueueConsumeHandler;
import cn.byteswalk.eaglemq.broker.model.TopicModel;

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


    public static void main(String[] args)
            throws IOException, InterruptedException {
        //加载配置 ，缓存对象的生成
//        ZonedDateTime start_1 = Instant.now().atZone(ZoneId.systemDefault());
//        initProperties();
//        ZonedDateTime end_1  = Instant.now().atZone(ZoneId.systemDefault());
//        logger.info("加载配置的开始时间 {}，结束时间： {}，用时：{} ms", start_1, end_1, Duration.between(start_1, end_1)
//                .toMillis());

//        ZonedDateTime start_2 = Instant.now().atZone(ZoneId.systemDefault());
//        String topic = "order_cancel_topic";
//        String consumeGroup = "user_service_group";
//        int j = 0;/**/
//        for (int i = 0; i < 5000; i++) {
//            byte[] content = consumeQueueConsumeHandler.consume(topic, consumeGroup, 0);
//            commitLogAppendHandler.appendMsg(topic, ("this is content " + i).getBytes());
//            logger.info("消费数据：{}", new String(content));
//            consumeQueueConsumeHandler.ack(topic, consumeGroup, 0);
//            logger.info("第 {} 写入数据", j);
//            TimeUnit.SECONDS.sleep(2);
//        }
//        ZonedDateTime end_2  = Instant.now().atZone(ZoneId.systemDefault());
//        logger.info("写入 {} 次数据开始时间 {}， 结束时间 {}， 用时： {} ms", j,
//                start_2, end_2, Duration.between(start_2, end_2).toMillis());

//        logger.info("读取内容：{}", commitLogAppendHandler.readMsg(topic));
//        initProperties();
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

//        logger.info("\33[32;4m开始多线程消费验证\33[0;4m");
        System.out.println("\33[32;4m开始多线程消费验证\33[0;4m");
    }
}
