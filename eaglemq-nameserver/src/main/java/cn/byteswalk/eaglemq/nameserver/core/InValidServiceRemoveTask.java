package cn.byteswalk.eaglemq.nameserver.core;

import cn.byteswalk.eaglemq.nameserver.commom.CommonCache;
import cn.byteswalk.eaglemq.nameserver.store.ServiceInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-13 14:30
 * @Description: 移除非正常服务任务
 * @Version: 1.0
 */
public class InValidServiceRemoveTask implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(InValidServiceRemoveTask.class);

    @Override
    public void run() {
        while (true) {
            try {
                // 先休眠三秒
                TimeUnit.SECONDS.sleep(3);
                Map<String, ServiceInstance> serviceInstanceMap = CommonCache.getServiceInstanceManager().getServiceInstanceMap();
                long currentTime = System.currentTimeMillis();
                Iterator<String> iterator = serviceInstanceMap.keySet().iterator();
                while (iterator.hasNext()) {
                    String brokerReqId = iterator.next();
                    ServiceInstance serviceInstance = serviceInstanceMap.get(brokerReqId);
                    if (serviceInstance.getLastHeartBeatTime() == null) {
                        continue;
                    }
                    // 超过9秒，移除异常的节点
                    if(currentTime - serviceInstance.getLastHeartBeatTime() > 3000 * 3) {
                        iterator.remove();
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}

