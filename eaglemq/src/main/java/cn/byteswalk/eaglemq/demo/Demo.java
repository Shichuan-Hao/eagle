package cn.byteswalk.eaglemq.demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class Demo {

    public static final String FILE_PATH = "";

    /**
     * 测试文件读取
     */
    public static void testMMapReadFile() {
        int _STEP_SIZE = 1024;
        try(FileChannel fileChannel = new RandomAccessFile(new File(FILE_PATH), "rw").getChannel();) {
            MappedByteBuffer map = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, _STEP_SIZE);
            System.out.println(map.isLoaded());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
