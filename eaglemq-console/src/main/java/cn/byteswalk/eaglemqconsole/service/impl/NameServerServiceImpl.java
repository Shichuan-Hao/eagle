package cn.byteswalk.eaglemqconsole.service.impl;

import cn.byteswalk.eaglemq.common.coder.TcpMsg;
import cn.byteswalk.eaglemq.common.dto.EagleMqBrokerDataDTO;
import cn.byteswalk.eaglemq.common.dto.QueryBrokerDataDTO;
import cn.byteswalk.eaglemq.common.enums.NameServerEventCode;
import cn.byteswalk.eaglemq.common.model.ConsumeQueueOffsetModel;
import cn.byteswalk.eaglemq.common.model.QueueModel;
import cn.byteswalk.eaglemq.common.model.TopicModel;
import cn.byteswalk.eaglemq.common.remote.NameServerNettyRemoteClient;
import cn.byteswalk.eaglemq.common.utils.AssertUtils;
import cn.byteswalk.eaglemqconsole.CommonUtils;
import cn.byteswalk.eaglemqconsole.service.INameServerService;
import cn.byteswalk.eaglemqconsole.vo.*;
import com.alibaba.fastjson.JSON;
import io.netty.util.internal.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-03 18:10
 * @Description:
 * @Version: 1.0
 */
@Service
public class NameServerServiceImpl implements INameServerService {

    @Resource
    private NameServerNettyRemoteClient nameServerNettyRemoteClient;

    //todo 所有nameserver上的数据缓存一份到本地内存中
    private EagleMqBrokerDataDTO queryTopicList() {
        QueryBrokerDataDTO queryBrokerDataDTO = new QueryBrokerDataDTO();
        queryBrokerDataDTO.setMsgId(UUID.randomUUID().toString());
        TcpMsg tcpMsg = nameServerNettyRemoteClient.sendSyncMsg(
                new TcpMsg(NameServerEventCode.QUERY_BROKER_DATA_INFO.getCode(),
                        JSON.toJSONBytes(queryBrokerDataDTO)), queryBrokerDataDTO.getMsgId());
        return JSON.parseObject(tcpMsg.getBody(), EagleMqBrokerDataDTO.class);
    }

    public List<TopicListResultVO> queryTopicInfos() {
        EagleMqBrokerDataDTO eagleMqBrokerDataDTO = this.queryTopicList();
        List<TopicModel> eagleMqTopicModelList = eagleMqBrokerDataDTO.getEagleMqTopicModelList();
        List<TopicListResultVO> topicListResultVOS = new ArrayList<>();
        for (TopicModel topicModel : eagleMqTopicModelList) {
            TopicListResultVO topicListResultVO = new TopicListResultVO();
            topicListResultVO.setTopic(topicModel.getTopicName());
            topicListResultVO.setQueueSize(topicModel.getQueueModels().size());
            topicListResultVO.setOffsetLimit(topicModel.getCommitLogModel().getOffsetLimit());
            topicListResultVO.setCurrentOffset(topicModel.getCommitLogModel().getOffset().get());
            topicListResultVO.setCreateTime(CommonUtils.secondToDate(topicModel.getCreateAt(), "yyyy-MM-dd HH:mm:ss"));
            topicListResultVOS.add(topicListResultVO);
        }
        return topicListResultVOS;
    }

    /**
     * 查询topic信息，分页查询
     *
     * @param topicInfoVO topicInfoVO
     * @return PageResponseVO<TopicListResultVO>
     */
    public PageResponseVO<TopicListResultVO> queryTopicInfoInPage(TopicInfoVO topicInfoVO) {
        Integer page = Optional.ofNullable(topicInfoVO.getPage())
                .orElseThrow(() -> new IllegalArgumentException("Invalid page number"));
        Integer pageSize = Optional.ofNullable(topicInfoVO.getPageSize())
                .filter(size -> size > 0 && size <= 100)
                .orElseThrow(() -> new IllegalArgumentException("Invalid page size"));

        List<TopicListResultVO> topicListResultVOS = this.queryTopicInfos();
        if (CollectionUtils.isEmpty(topicListResultVOS)) {
            return PageResponseVO.emptyPage();
        }

        // Filter topics in-memory
        String topicFilter = topicInfoVO.getTopic();
        if (!StringUtil.isNullOrEmpty(topicFilter)) {
            topicListResultVOS = topicListResultVOS.stream()
                    .filter(item -> item.getTopic().contains(topicFilter))
                    .collect(Collectors.toList());
        }

        int totalCount = topicListResultVOS.size();
        int beginLimit = Math.min((page - 1) * pageSize, totalCount);
        int endLimit = Math.min(page * pageSize, totalCount);

        List<TopicListResultVO> currentPageData = topicListResultVOS.subList(beginLimit, endLimit);
        int totalPage = (totalCount + pageSize - 1) / pageSize; // Calculating total pages

        return PageResponseVO.success(currentPageData, totalPage, page, pageSize);
    }

    /**
     * 查询消费组信息 分页查询
     *
     * @param consumerInfoReqVO consumerInfoReqVO
     * @return List<ConsumerInfoResultVO>
     */
    public List<ConsumerInfoResultVO> queryConsumerInfo(ConsumerInfoReqVO consumerInfoReqVO) {
        AssertUtils.isNotBlank(consumerInfoReqVO.getTopic(), "topic can not be empty");
        EagleMqBrokerDataDTO eagleMqBrokerDataDTO = this.queryTopicList();
        ConsumeQueueOffsetModel consumeQueueOffsetModel = eagleMqBrokerDataDTO.getConsumeQueueOffsetModel();
        Map<String, TopicModel> eagleMqTopicModelMap = eagleMqBrokerDataDTO.getEagleMqTopicModelList().stream()
                .collect(Collectors.toMap(TopicModel::getTopicName, item -> item));
        ConsumeQueueOffsetModel.OffsetTable offsetTable = consumeQueueOffsetModel.getOffsetTable();
        //所有的消费组信息
        List<ConsumerInfoResultVO> consumerInfoResultVOS = new ArrayList<>();
        String queryTopicName = consumerInfoReqVO.getTopic();
        //必须要输入topic信息
        Map<String, ConsumeQueueOffsetModel.ConsumerGroupDetail> consueGroupMap = offsetTable.getTopicConsumerGroupDetail();
        for (String topic : consueGroupMap.keySet()) {
            if (!topic.contains(queryTopicName)) {
                continue;
            }
            ConsumeQueueOffsetModel.ConsumerGroupDetail consumerGroupDetail = consueGroupMap.get(topic);
            for (String consumerGroup : consumerGroupDetail.getConsumerGroupDetailMap().keySet()) {
                Map<String, String> queueOffsetDetail = consumerGroupDetail.getConsumerGroupDetailMap().get(consumerGroup);
                for (String queueIdStr : queueOffsetDetail.keySet()) {
                    String queueOffsetStr = queueOffsetDetail.get(queueIdStr);
                    Integer queueId = Integer.valueOf(queueIdStr);
                    ConsumerInfoResultVO consumerInfoResultVO = new ConsumerInfoResultVO();
                    consumerInfoResultVO.setConsumerGroup(consumerGroup);
                    consumerInfoResultVO.setTopic(topic);
                    String[] offsetArr = queueOffsetStr.split("#");
                    consumerInfoResultVO.setLastOffset(Integer.valueOf(offsetArr[0]));
                    consumerInfoResultVO.setCurrentOffset(Integer.valueOf(offsetArr[1]));
                    consumerInfoResultVO.setQueueId(queueId);
//                    QueueModel queueModel = eagleMqTopicModelMap.get(topic).getQueueModels().get(queueId);
//                    if (queueModel != null) {
//                        consumerInfoResultVO.setOffsetLimit(queueModel.getMaxOffset());
//                    }
                    consumerInfoResultVOS.add(consumerInfoResultVO);
                }
            }
        }
        return consumerInfoResultVOS;
    }
}

