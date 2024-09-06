package cn.byteswalk.eaglemqconsole.controller;

import cn.byteswalk.eaglemqconsole.service.INameServerService;
import cn.byteswalk.eaglemqconsole.vo.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-03 18:09
 * @Description:
 * @Version: 1.0
 */
@RestController
@RequestMapping("/console")
public class ConsoleController {

    @Resource
    private INameServerService nameServerService;

    @PostMapping("/queryTopicInfoInPage")
    public PageResponseVO<TopicListResultVO> queryTopicInfoInPage(TopicInfoVO topicInfoVO) {
        return nameServerService.queryTopicInfoInPage(topicInfoVO);
    }

    @PostMapping("/queryConsumerInfo")
    public BaseResponseVO<List<ConsumerInfoResultVO>> queryConsumerInfo(ConsumerInfoReqVO consumerInfoReqVO) {
        return BaseResponseVO.success(nameServerService.queryConsumerInfo(consumerInfoReqVO));
    }

}

