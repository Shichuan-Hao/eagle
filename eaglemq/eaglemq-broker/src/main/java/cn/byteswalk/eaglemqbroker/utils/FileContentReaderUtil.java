package cn.byteswalk.eaglemqbroker.utils;


import java.io.BufferedReader;
import java.io.FileReader;

/**
 * 简化版本的文件读取工具
 */
public class FileContentReaderUtil {

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

}
