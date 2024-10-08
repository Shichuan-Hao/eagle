package cn.byteswalk.eaglemq.common.constants;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-05 13:10
 * @Description: Broker 常量类
 * @Version: 1.0
 */
public class BrokerConstants {

    public static final String  EAGLE_MQ_HOME                                  = "EAGLE_MQ_HOME";
    public static final String  BASE_COMMIT_PATH                               = "/commitlog/";
    public static final String  BASE_CONSUME_QUEUE_PATH                        = "/consumequeue/";
    public static final String  BROKER_PROPERTIES_PATH                         = "/config/broker.properties";
    public static final String  SPLIT                                          = "/";
    public static final Integer COMMIT_LOG_DEFAULT_MMAP_SIZE                   = 1024 * 1024; // 1mb单位，方便测试
    public static final Integer DEFAULT_REFRESH_MQ_TOPIC_TIME_STEP             = 3;
    public static final Integer DEFAULT_REFRESH_CONSUME_QUEUE_OFFSET_TIME_STEP = 1;
    public static final int     CONSUME_QUEUE_EACH_MSG_SIZE                    = 12;
    public static final short   DEFAULT_MAGIC_NUM                              = 17671;
}
