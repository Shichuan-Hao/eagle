package cn.byteswalk.eaglemq.common.utils;

import java.util.List;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-06 14:27
 * @Description: 断言工具
 * @Version: 1.0
 */
public class AssertUtils {

    public static void isNotBlank(String val, String msg) {
        if (val == null || val.trim().isEmpty()) {
            throw new RuntimeException(msg);
        }
    }

    public static void isNotEmpty(List list, String msg) {
        if (list == null || list.isEmpty()) {
            throw new RuntimeException(msg);
        }
    }


    public static void isNotNull(Object val, String msg) {
        if (val == null) {
            throw new RuntimeException(msg);
        }
    }

    public static void isTrue(Boolean condition, String msg) {
        if (!condition) {
            throw new RuntimeException(msg);
        }
    }

}

