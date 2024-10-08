package cn.byteswalk.eaglemq.nameserver.event.model;

import com.alibaba.fastjson.JSON;

/**
 * @Author idea
 * @Date: Created in 14:19 2024/5/4
 * @Description 心跳事件
 */
public class HeartBeatEvent extends Event {

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
