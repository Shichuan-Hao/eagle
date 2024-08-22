package cn.byteswalk.eaglemqbroker.core;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class MessageAppendHandler {

    private final MMapFileModelManager mMapFileModelManager = new MMapFileModelManager();

    private static final String FILE_PATH = "E:\\bytewalk\\jproject\\eagle\\eaglemq\\broker\\store\\pay_topic\\00000000";
    private static final String TOPIC_NAME = "pay_topic";

    public MessageAppendHandler()
            throws IOException {
        this.prepareMMapLoading();
    }

    public void prepareMMapLoading()
            throws IOException {
        MMapFileModel mMapFileModel = new MMapFileModel();
        mMapFileModel.loadFileInMMap(FILE_PATH, 0, 1024 * 1024);
        mMapFileModelManager.put(TOPIC_NAME, mMapFileModel);
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


    public static void main(String[] args)
            throws IOException {
        MessageAppendHandler messageAppendHandler = new MessageAppendHandler();
        messageAppendHandler.appendMsg(TOPIC_NAME, "哈this is test\n这是测试内容");
        System.out.println(messageAppendHandler.readMes(TOPIC_NAME));
    }
}
