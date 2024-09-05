package cn.byteswalk.eaglemq.common.constants;

/**
 * Broker 常量类
 */
public class CommonConstants {

    public static final String EAGLE_MQ_HOME = "eagle_mq_home";
    public static final String BASE_STORE_PATH = "/broker/store/";
    public static final Integer COMMIT_DEFAULT_MMAP_SIZE = 1024 * 1024;  // 通常为1gb，但是方便开发和调试设置 1mb
    public static final Integer DEFAULT_REFRESH_MQ_TOPIC_TIME_STEP = 3;
    public static final Integer START_OFFSET = 0;


    public static final String TEST_PATH = "E:/bytewalk/jproject/eaglemq/broker/store/order_cancel_topic/00000000";
    public static final short DEFAULT_MAGIC_NUM = 17671;
}
