package cn.byteswalk.eaglemqbroker;

import cn.byteswalk.eaglemqbroker.cache.CommonCache;
import cn.byteswalk.eaglemqbroker.config.GlobalPropertiesLoader;
import cn.byteswalk.eaglemqbroker.config.TopicLoader;
import cn.byteswalk.eaglemqbroker.core.CommitLogAppendHandler;
import cn.byteswalk.eaglemqbroker.model.TopicModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

public class BrokerStartUp {

    private static final Logger logger = LoggerFactory.getLogger(BrokerStartUp.class);

    private static GlobalPropertiesLoader globalPropertiesLoader;
    private static TopicLoader topicLoader;
    private static CommitLogAppendHandler commitLogAppendHandler;

    /**
     * 初始化配置
     */
    private static void initProperties()
            throws IOException {

        globalPropertiesLoader = new GlobalPropertiesLoader();
        globalPropertiesLoader.loadProperties();

        // 创建 topic 属性加载器
        topicLoader = new TopicLoader();
        // 加载 topic 的属性
        topicLoader.loadProperties();
        // 刷新 topic 的信息
        topicLoader.startRefreshTopicInfoTask();

        commitLogAppendHandler = new CommitLogAppendHandler();

        for (TopicModel topicModel : CommonCache.getTopicModelMap().values()) {
            String topicName = topicModel.getTopicName();
            // 预加载（映射）
            commitLogAppendHandler.prepareMMapLoading(topicName);
        }

    }


    public static void main(String[] args)
            throws IOException, InterruptedException {
        //加载配置 ，缓存对象的生成
        ZonedDateTime start_1 = Instant.now().atZone(ZoneId.systemDefault());
        initProperties();
        ZonedDateTime end_1  = Instant.now().atZone(ZoneId.systemDefault());
        logger.info("加载配置的开始时间 {}，结束时间： {}，用时：{} ms", start_1, end_1, Duration.between(start_1, end_1)
                .toMillis());

        ZonedDateTime start_2 = Instant.now().atZone(ZoneId.systemDefault());
        String topic = "order_cancel_topic";
        int j = 0;
        for (int i = 0; i < 10; i++) {
            commitLogAppendHandler.appendMsg(topic, ("this is content " + (i + 1)).getBytes());
            j = i + 1;
            logger.info("第 {} 写入数据", j);
            TimeUnit.SECONDS.sleep(5);
        }
        ZonedDateTime end_2  = Instant.now().atZone(ZoneId.systemDefault());
        logger.info("写入 {} 次数据开始时间 {}， 结束时间 {}， 用时： {} ms", j,
                start_2, end_2, Duration.between(start_2, end_2).toMillis());

        logger.info("读取内容：{}", commitLogAppendHandler.readMsg(topic));
    }
}
