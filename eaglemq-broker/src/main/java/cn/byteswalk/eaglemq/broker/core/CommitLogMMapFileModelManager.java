package cn.byteswalk.eaglemq.broker.core;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class CommitLogMMapFileModelManager {

    /**
     * key:主题名称，value:文件的 MMapFileModel 对象
     */
    private final Map<String, CommitLogMMapFileModel> mMapFileModelMap = new HashMap<>();

    public void  put(String topic, CommitLogMMapFileModel commitLogMMapFileModel) {
        mMapFileModelMap.put(topic, commitLogMMapFileModel);
    }

    public CommitLogMMapFileModel get(String topic) {
        return mMapFileModelMap.get(topic);
    }
}
