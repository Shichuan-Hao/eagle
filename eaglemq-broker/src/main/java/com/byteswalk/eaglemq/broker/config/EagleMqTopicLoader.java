package com.byteswalk.eaglemq.broker.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.byteswalk.eaglemq.broker.cache.CommonCache;
import com.byteswalk.eaglemq.broker.model.EagleMqTopicModel;
import io.netty.util.internal.StringUtil;
import com.byteswalk.eaglemq.broker.utils.FileContentUtil;
import cn.byteswalk.eaglemq.common.constants.BrokerConstants;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author idea
 * @Date: Created in 08:54 2024/3/26
 * @Description 负责将mq的主题配置信息加载到内存中
 */
public class EagleMqTopicLoader {

    private String filePath;

    public void loadProperties() {
        GlobalProperties globalProperties = CommonCache.getGlobalProperties();
        String basePath = globalProperties.getEagleMqHome();
        if(StringUtil.isNullOrEmpty(basePath)){
            throw new IllegalArgumentException("EAGLE_MQ_HOME is invalid!");
        }
        filePath = basePath + "/config/eaglemq-topic.json";
        String fileContent = FileContentUtil.readFromFile(filePath);
        List<EagleMqTopicModel> eagleMqTopicModelList = JSON.parseArray(fileContent,EagleMqTopicModel.class);
        CommonCache.setEagleMqTopicModelList(eagleMqTopicModelList);
    }

    /**
     * 开启一个刷新内存到磁盘的任务
     */
    public void startRefreshEagleMqTopicInfoTask() {
        //异步线程
        //每3秒将内存中的配置刷新到磁盘里面
        CommonThreadPoolConfig.refreshEagleMqTopicExecutor.execute(() -> {
            do {
                try {
                    TimeUnit.SECONDS.sleep(BrokerConstants.DEFAULT_REFRESH_MQ_TOPIC_TIME_STEP);
//                        System.out.println("commitlog写入情况刷新磁盘");
                    List<EagleMqTopicModel> eagleMqTopicModelList = CommonCache.getEagleMqTopicModelList();
                    FileContentUtil.overWriteToFile(filePath,JSON.toJSONString(eagleMqTopicModelList, SerializerFeature.PrettyFormat));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }while (true);
        });

    }
}
