package cn.byteswalk.eaglemq.broker;

import org.junit.Test;

import java.io.*;

public class FileInputOutput {


    @Test
    public void testFileInputOutput() {
        File file = new File("");
        try (InputStream in = new FileInputStream(file);
             OutputStream out = new FileOutputStream(file);) {

            byte[] content = new byte[1024];
            in.read(content);
            System.out.println(new String(content));

            out.write(("test-content1-" + System.currentTimeMillis()).getBytes());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFileReaderWriter() throws IOException {
        File file = new File("");
        // 创建 FileWriter 对象
        FileWriter writer = new FileWriter(file);
        // 向文件中写入内容
        writer.write("test-content2-" + System.currentTimeMillis());
        writer.flush();
        writer.close();

        // 创建FileReader对象，读取文件中的内容
        FileReader reader = new FileReader(file);
        char[] ch = new char[100];
        reader.read(ch);
        for (char c : ch) {
            System.out.println(c);
        }
        reader.close();
    }
}
