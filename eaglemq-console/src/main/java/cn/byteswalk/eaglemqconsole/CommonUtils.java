package cn.byteswalk.eaglemqconsole;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-06 11:14
 * @Description: 通用工具类
 * @Version: 1.0
 */
public class CommonUtils {

    public static String secondToDate(long second, String patten) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(second); // 转换为毫秒
        Date time = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(patten);
        return simpleDateFormat.format(time);
    }
}

