package cn.byteswalk.eaglemqbroker.utils;

import cn.byteswalk.eaglemqbroker.model.QueueModel;
import cn.byteswalk.eaglemqbroker.model.TopicModel;
import com.alibaba.fastjson2.JSON;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

/**
 * 简化版本的文件读取工具
 */
public class FileContentReaderUtils {

    private static String readFromFile(String path){
        // FileInputStream 字节流
        // FileReader 字符流
        try(BufferedReader in = new BufferedReader(new FileReader(path))) {
            StringBuffer stringBuffer = new StringBuffer();
            while (in.ready()) {
                stringBuffer.append(in.readLine());
            }
            return stringBuffer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String content = FileContentReaderUtils.readFromFile("E:\\bytewalk\\jproject\\eagle\\eaglemq\\broker\\config\\eaglemq-topic.json");
        System.out.println(content);
        List<TopicModel> topicModels = JSON.parseArray(content, TopicModel.class);
        System.out.println(topicModels);
    }
}
