package cn.byteswalk.eaglemq.broker;


import cn.byteswalk.eaglemq.broker.utils.MMapUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * @Author hsc
 * @Date: Created in 20:18 2024/3/24
 * @Description
 */
public class MMapUtilTests {

    private MMapUtil mapUtil;
    private static final String file = "E:\\bytewalk\\jproject\\eagle\\eaglemq\\broker\\store\\order_cancel_topic\\00000000";

    @Before
    public void setUp() throws IOException {
        mapUtil = new MMapUtil();
        mapUtil.loadFileInMMap(file, 0, 1 * 1024 * 100);
        System.out.println("文件映射内存成功");
    }

    @Test
    public void testWriteAndReadFile() {
//        String str = "this is a test content这是测试内容";
        String str = "这是测试内容";
        byte[] content = str.getBytes();
        mapUtil.writeContent(content);
        // consumeQueue
        byte[] readContent = mapUtil.readContent(0, content.length);
        System.out.println(new String(readContent));
        mapUtil.clean();
    }
}
