package com.byteswalk.eaglemq.nameserver.utils;

import com.byteswalk.eaglemq.nameserver.common.CommonCache;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-10-09 17:07
 * @Description: NameserverUtils
 * @Version: 1.0
 */
public class NameserverUtils {

    public static boolean isVerify(String user, String password) {
        String rightUser = CommonCache.getNameserverProperties().getNameserverUser();
        String rightPassword = CommonCache.getNameserverProperties().getNameserverPwd();
        return rightUser.equals(user) && rightPassword.equals(password);
    }

}

