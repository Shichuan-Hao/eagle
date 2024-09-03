package cn.byteswalk.eaglemqbroker.utils;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 简化版本的文件读写工具
 */
public class FileContentUtil {

    /**
     * 读取文件内容
     * @param path 文件路径
     * @return 返回文件的内容
     */
    public static String readFromFile(String path){
        // FileInputStream 字节流
        // FileReader 字符流
        try(BufferedReader in = new BufferedReader(new FileReader(path))) {
            StringBuilder sb = new StringBuilder();
            while (in.ready()) {
                sb.append(in.readLine());
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 覆盖
     * @param path 文件路径
     * @param content 消息内容
     */
    public static void overWriteToFile(String path, String content) {
        try (FileWriter fileWriter = new FileWriter(path)) {
            fileWriter.write(content);
            fileWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
