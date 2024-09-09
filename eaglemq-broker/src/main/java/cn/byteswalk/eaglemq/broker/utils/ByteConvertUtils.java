package cn.byteswalk.eaglemq.broker.utils;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-08-27 15:02
 * @Description: 字节数组转换工具
 * @Version: 1.0
 */
public class ByteConvertUtils {


    /**
     *
     * @param source 原数组
     * @param pos 位置
     * @param len 长度
     * @return byte[]
     */
    public static byte[] readInPos(byte[] source, int pos, int len) {
        byte[] result = new byte[len];
        for (int i = pos, j = 0; i < pos + len; i++) {
            result[j++] = source[i];
        }
        return result;
    }


    /**
     * 将 int 类型转换为字节数组
     * @param value int 类型的
     * @return byte[]
     */
    public static byte[] intToBytes(int value) {
        byte[] src = new byte[4];
        //32位-24位=8位
        //00000000001 0xFF 16
        src[3] = (byte) ((value >> 24) & 0xFF);
        src[2] = (byte) ((value >> 16) & 0xFF);
        src[1] = (byte) ((value >> 8) & 0xFF);
        src[0] = (byte) (value & 0xFF);
        return src;
    }

    /**
     * 字节数组转换为int
     * @param ary bytes字节数组
     * @return 返回字节数组
     */
    public static int bytesToInt(byte[] ary) {
        int value;
        value = (int) ((ary[0] & 0xFF)
                | ((ary[1] << 8) & 0xFF00)
                | ((ary[2] << 16) & 0xFF0000)
                | ((ary[3] << 24) & 0xFF000000));
        return value;
    }

    public static void main(String[] args) {
        int j = 100;
        //4个字节，1byte=8bit, byte[4]
        byte[] byteContent = ByteConvertUtils.intToBytes(j);
        System.out.println(byteContent.length);
        int result = ByteConvertUtils.bytesToInt(byteContent);
        System.out.println(result);
    }
}

