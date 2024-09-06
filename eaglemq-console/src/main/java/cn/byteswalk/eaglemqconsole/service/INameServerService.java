package cn.byteswalk.eaglemqconsole.service;

import cn.byteswalk.eaglemqconsole.vo.*;

import java.util.Arrays;
import java.util.List;

public interface INameServerService {


    PageResponseVO<TopicListResultVO> queryTopicInfoInPage(TopicInfoVO topicInfoVO);

    List<ConsumerInfoResultVO> queryConsumerInfo(ConsumerInfoReqVO consumerInfoReqVO);
}
