package cn.byteswalk.eaglemqbroker;

import cn.byteswalk.eaglemqbroker.cache.CommonCache;
import cn.byteswalk.eaglemqbroker.config.GlobalPropertiesLoader;
import cn.byteswalk.eaglemqbroker.config.TopicLoader;
import cn.byteswalk.eaglemqbroker.core.CommitLogAppendHandler;
import cn.byteswalk.eaglemqbroker.model.TopicModel;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class BrokerStartUp {

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
        Instant start_1 = Instant.now();
        initProperties();
        Instant end_1  = Instant.now();
        System.out.println("cost time: " + Duration.between(start_1, end_1).toMillis() + "ms");

        Instant start_2 = Instant.now();
        String topic = "order_cancel_topic";
        for (int i = 0; i < 50000; i++) {
            commitLogAppendHandler.appendMsg(topic, ("this is content " + (i + 1)).getBytes());
            System.out.println("第 " + (i + 1) + " 写入数据");
            TimeUnit.SECONDS.sleep(1);
        }
        Instant end_2  = Instant.now();
        System.out.println("cost time: " + Duration.between(start_2, end_2).toMillis() + "ms");

        System.out.println(commitLogAppendHandler.readMsg(topic));
    }
}
