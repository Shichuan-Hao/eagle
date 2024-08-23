package cn.byteswalk.eaglemqbroker.core;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class CommitLogAppendHandler {

    private final MMapFileModelManager mMapFileModelManager = new MMapFileModelManager();

//    private static final String FILE_PATH = "E:\\bytewalk\\jproject\\eagle\\eaglemq\\broker\\store\\pay_topic\\00000000";
//    private static final String TOPIC_NAME = "pay_topic";

    public void prepareMMapLoading(String filePath, String topicName)
            throws IOException {
        MMapFileModel mMapFileModel = new MMapFileModel();
        mMapFileModel.loadFileInMMap(filePath, 0, 1024 * 1024);
        mMapFileModelManager.put(topicName, mMapFileModel);
    }

    public String readMes(String topic) {
        MMapFileModel mMapFileModel = mMapFileModelManager.get(topic);

        checkMMapFileModelIsNull(mMapFileModel);

        byte[] readContent = mMapFileModel.readContent(0, 10);
        return new String(readContent);
    }

    public void appendMsg(String topic, String content) {
        MMapFileModel mMapFileModel = mMapFileModelManager.get(topic);

        checkMMapFileModelIsNull(mMapFileModel);

        mMapFileModel.writeContent(content.getBytes(StandardCharsets.UTF_8));
    }

    private void checkMMapFileModelIsNull(MMapFileModel mMapFileModel) {
        if (Objects.isNull(mMapFileModel)) {
            throw new RuntimeException("Topic is invalid!");
        }
    }

}
