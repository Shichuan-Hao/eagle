package cn.byteswalk.eaglemq.common.utils;

import jdk.nashorn.internal.runtime.linker.Bootstrap;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-10-09 16:26
 * @Description: 断言工具
 * @Version: 1.0
 */
public class AssertUtils {

    public static void isNotBlank(String val, String msg) {
        if (val == null || val.trim().isEmpty()) {
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

