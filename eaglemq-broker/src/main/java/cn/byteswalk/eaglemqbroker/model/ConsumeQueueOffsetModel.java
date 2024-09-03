package cn.byteswalk.eaglemqbroker.model;

import java.util.Map;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-03 15:09
 * @Description:
 * @Version: 1.0
 */
public class ConsumeQueueOffsetModel {

    private OffsetTable offsetTable;

    private class OffsetTable {
        private Map<String,ConsumerGroupDetail> topicConsumerGroupDetail;

        public Map<String, ConsumerGroupDetail> getTopicConsumerGroupDetail() {
            return topicConsumerGroupDetail;
        }

        public void setTopicConsumerGroupDetail(Map<String, ConsumerGroupDetail> topicConsumerGroupDetail) {
            this.topicConsumerGroupDetail = topicConsumerGroupDetail;
        }
    }

    private class ConsumerGroupDetail {
        private Map<String,Map<String,String>> consumerGroupDetailMap;

        public Map<String, Map<String, String>> getConsumerGroupDetailMap() {
            return consumerGroupDetailMap;
        }

        public void setConsumerGroupDetailMap(Map<String, Map<String, String>> consumerGroupDetailMap) {
            this.consumerGroupDetailMap = consumerGroupDetailMap;
        }
    }

    public OffsetTable getOffsetTable() {
        return offsetTable;
    }

    public void setOffsetTable(OffsetTable offsetTable) {
        this.offsetTable = offsetTable;
    }
}


