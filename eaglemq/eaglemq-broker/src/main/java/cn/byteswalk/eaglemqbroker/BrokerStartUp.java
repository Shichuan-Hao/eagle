package cn.byteswalk.eaglemqbroker;

import cn.byteswalk.eaglemqbroker.cache.CommonCache;
import cn.byteswalk.eaglemqbroker.config.GlobalPropertiesLoader;
import cn.byteswalk.eaglemqbroker.config.TopicLoader;
import cn.byteswalk.eaglemqbroker.core.CommitLogAppendHandler;
import cn.byteswalk.eaglemqbroker.model.TopicModel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

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
        topicLoader = new TopicLoader();
        topicLoader.loadProperties();
        commitLogAppendHandler = new CommitLogAppendHandler();

        for (TopicModel topicModel : CommonCache.getTopicModelMap().values()) {
            String topicName = topicModel.getTopicName();
            // 预加载（映射）
            commitLogAppendHandler.prepareMMapLoading(topicName);
        }

    }


    public static void main(String[] args) throws IOException {
        // 模拟启动流程
        // 第一步：加载配置，换成对象生产
        initProperties();
        // 模拟初始话文件映射
        String topic = "order_cancel_topic";
        commitLogAppendHandler.appendMsg(topic, "this is a test contest".getBytes(StandardCharsets.UTF_8));
        System.out.println(commitLogAppendHandler.readMes(topic));
    }
}
