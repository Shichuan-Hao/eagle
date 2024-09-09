package cn.byteswalk.eaglemq.broker.config;

import cn.byteswalk.eaglemq.broker.constants.BrokerConstants;
import cn.byteswalk.eaglemq.broker.cache.CommonCache;
import cn.byteswalk.eaglemq.broker.model.ConsumeQueueOffsetModel;
import cn.byteswalk.eaglemq.broker.utils.FileContentUtil;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson2.JSON;

import io.netty.util.internal.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-03 15:55
 * @Description:
 * @Version: 1.0
 */
public class ConsumeQueueOffsetLoader {

    private final Logger log = LoggerFactory.getLogger(ConsumeQueueOffsetLoader.class);

    private String filePath;

    public void loadProperties() {
        GlobalProperties globalProperties = CommonCache.getGlobalProperties();
        String basePath = globalProperties.getEagleMqHome();
        if (StringUtil.isNullOrEmpty(basePath)) {
            throw new IllegalArgumentException("EAGLE_MQ_HOME is invalid!");
        }
        filePath = basePath + BrokerConstants.CONSUME_QUEUE_OFFSET_CONFIG;
        String fileContent = FileContentUtil.readFromFile(filePath);
        ConsumeQueueOffsetModel consumeQueueOffsetModel = JSON.parseObject(fileContent, ConsumeQueueOffsetModel.class);
        CommonCache.setConsumeQueueOffsetModel(consumeQueueOffsetModel);
    }

    /**
     * 开启一个刷新内存到磁盘的认为
     */
    public void startRefreshConsumeQueueOffsetTask() {
        // 异步线程
        // 每隔 n秒（可以加入配置文件中）将内存中的配置刷新到磁盘里面（mmap 对小文件）
        // 类似 Redis rdb 持久化思路（并发）
        CommonThreadPoolConfig.refreshConsumeQueueOffsetExecutor.execute(new Runnable() {
            @Override
            @SuppressWarnings("InfiniteLoopStatement")
            public void run() {
                do {
                    try {
                        TimeUnit.SECONDS.sleep(BrokerConstants.DEFAULT_REFRESH_MQ_CONSUMER_QUEUE_OFFSET_STEP);
                        ConsumeQueueOffsetModel consumeQueueOffsetModel = CommonCache.getConsumeQueueOffsetModel();
                        String prettyFormat = String.valueOf(SerializerFeature.PrettyFormat);
                        String consumeQueueOffsetModelStr = JSON.toJSONString(consumeQueueOffsetModel, prettyFormat);
                        FileContentUtil.overWriteToFile(filePath, consumeQueueOffsetModelStr);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } while (true);
            }
        });

    }
}

