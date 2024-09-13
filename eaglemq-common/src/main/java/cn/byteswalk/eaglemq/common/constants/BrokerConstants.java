package cn.byteswalk.eaglemq.common.constants;

/**
 * Broker 常量类
 */
public class BrokerConstants {

    public static final String EAGLE_MQ_HOME = "eagle_mq_home";
    public static final String BASE_COMMITLOG_PATH = "/broker/commitlog/";
    public static final String BASE_CONSUME_QUEUE_PATH = "/broker/consumequeue/";
    public static final Integer COMMIT_DEFAULT_MMAP_SIZE = 1024 * 1024;  // 通常为1gb，但是方便开发和调试设置 1mb
    public static final Integer DEFAULT_REFRESH_MQ_TOPIC_TIME_STEP = 3;
    public static final Integer DEFAULT_REFRESH_MQ_CONSUMER_QUEUE_OFFSET_STEP = 1;
    public static final Integer START_OFFSET = 0;
    public static final String SPLIT = "/";
    public static final String CONSUME_QUEUE_OFFSET_CONFIG = "/broker/config/consumequeue-offset.json";
    public static final String COMMITLOG_CONFIG = "/broker/config/eaglemq-topic.json";


    public static final String TEST_PATH = "E:/bytewalk/jproject/eaglemq/broker/commitlog/order_cancel_topic/00000000";
}
