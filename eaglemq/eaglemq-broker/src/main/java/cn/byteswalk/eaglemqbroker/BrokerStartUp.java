package cn.byteswalk.eaglemqbroker;

import cn.byteswalk.eaglemqbroker.cache.CommonCache;
import cn.byteswalk.eaglemqbroker.config.GlobalPropertiesLoader;
import cn.byteswalk.eaglemqbroker.config.TopicLoader;
import cn.byteswalk.eaglemqbroker.constants.BrokerConstants;
import cn.byteswalk.eaglemqbroker.core.CommitLogAppendHandler;
import cn.byteswalk.eaglemqbroker.model.TopicModel;

import java.io.IOException;
import java.util.List;

public class BrokerStartUp {

    private static GlobalPropertiesLoader globalPropertiesLoader;
    private static TopicLoader topicLoader;
    private static CommitLogAppendHandler commitLogAppendHandler;

    /**
     * 初始化配置
     */
    private static void initProperties() throws IOException {
        globalPropertiesLoader = new GlobalPropertiesLoader();
        globalPropertiesLoader.loadProperties();
        topicLoader = new TopicLoader();
        topicLoader.loadProperties();
        commitLogAppendHandler = new CommitLogAppendHandler();

        List<TopicModel> topicModelList = CommonCache.getTopicInfo();
        for (TopicModel topicModel : topicModelList) {
            String topicName = topicModel.getTopic();
            String filePath = CommonCache.getGlobalProperties().getEagleMqHome()
                    + BrokerConstants.BASE_STORE_PATH
                    + topicName
                    + "/0000001";
            commitLogAppendHandler.prepareMMapLoading(filePath, topicName);
        }

    }


    public static void main(String[] args) throws IOException {
        // 模拟启动流程
        // 第一步：加载配置，换成对象生产
        initProperties();
        // 模拟初始话文件映射
        String topic = "order_cancel_topic";
        commitLogAppendHandler.appendMsg(topic, "this is a test contest");
        System.out.println(commitLogAppendHandler.readMes(topic));
    }
}
