package cn.byteswalk.eaglemqbroker.core;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class MMapFileModelManager {

    /**
     * key:主题名称，value:文件的 MMapFileModel 对象
     */
    private final Map<String, MMapFileModel> mMapFileModelMap = new HashMap<>();

    public void put(String topic, MMapFileModel mMapFileModel) {
        mMapFileModelMap.put(topic, mMapFileModel);
    }

    public MMapFileModel get(String topic) {
        return mMapFileModelMap.get(topic);
    }
}
