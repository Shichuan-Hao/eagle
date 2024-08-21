package cn.byteswalk.eaglemqbroker.utils;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;

import java.nio.channels.FileChannel;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * @Author hsc
 * @Date Create in 15:09 2024/7/23
 * @Description 支持基于 Java 的 MMap api 访问文件能力（文件的读写的能力）
 * <p>
 * <p>
 * 支持指定的 offset 的文件映射（结束映射的 offset-开始映射的 offset = 映射的内存体积） done!
 * 文件从指定的 offset 开始读取 done!
 * 文件从指定的 offset 开始写入 done!
 * 文件映射后的内存释放
 */
public class MMapUtil {

    private static final String RW_ACCESS_MODE = "rw";

    private File file;
    // MappedByteBuffer 是 DirectByteBuffer，即直接内存
    // DirectByteBuffer 特点之一是不受限制于 JVM 的 GC 回收的管理，意味着将这个对象设置为 null
    private MappedByteBuffer mappedByteBuffer;
    private FileChannel fileChannel;

    /**
     * 指定offset做文件映射
     *
     * @param filePath    文件路径
     * @param startOffset 开始映射的offset
     * @param mappedSize  映射的体积（endOffset-startOffset）
     */
    public void loadFileInMMap(String filePath, int startOffset, int mappedSize)
            throws IOException {
        this.file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("filePath is " + filePath + " inValid");
        }
        // noinspection resource
        this.fileChannel = new RandomAccessFile(file, RW_ACCESS_MODE).getChannel();
        // MapMode:映射模式|position:开始读的位置|size:读取内容的大小
        if (mappedSize > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Size exceeds Integer.MAX_VALUE");
        }
        this.mappedByteBuffer = this.fileChannel.map(FileChannel.MapMode.READ_WRITE, startOffset, mappedSize);
    }


    /**
     * 支持从文件从指定的 offset 开始读取内容
     *
     * @param readOffset 读取内容的起始位置
     * @param size       读取内容的大小
     * @return 返回读取内容
     */
    public byte[] readContent(int readOffset, int size) {
        mappedByteBuffer.position(readOffset);
        byte[] content = new byte[size];
        int j = 0;
        for (int i = 0; i < size; i++) {
            // 从内存里访问，快速高效，不用担心
            byte b = mappedByteBuffer.get(readOffset + i);
            content[j++] = b;
        }
        return content;
    }

    /**
     * 更高性能的一种写入api
     *
     * @param content 内容
     */
    public void writeContent(byte[] content) {
        this.writeContent(content, false);
    }

    /**
     * 写入数据到磁盘中
     *
     * @param content 内容
     * @param force   强制刷盘标志
     */
    public void writeContent(byte[] content, boolean force) {
        // 默认刷到 page cache 中
        // 如果需要强制刷盘，这里要兼容
        mappedByteBuffer.put(content);
        if (force) {
            // 强制刷盘
            mappedByteBuffer.force();
        }
    }

    public void clean() {
        if (mappedByteBuffer == null || !mappedByteBuffer.isDirect() || mappedByteBuffer.capacity() == 0) {
            return;
        }
        invoke(invoke(viewed(mappedByteBuffer), "cleaner"), "clean");
    }

    // AccessController.doPrivileged 在 jdk17 已经过时
    private Object invoke(final Object target, final String methodName, final Class<?>... args) {
        return AccessController.doPrivileged(new PrivilegedAction<Object>() {
            @Override
            public Object run() {
                try {
                    Method method = method(target, methodName, args);
                    method.setAccessible(true);
                    return method.invoke(target);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private Method method(Object target, String methodName, Class<?>[] args)
            throws NoSuchMethodException {
        try {
            return target.getClass().getMethod(methodName, args);
        } catch (NoSuchMethodException e) {
            return target.getClass().getDeclaredMethod(methodName, args);
        }
    }

    private ByteBuffer viewed(ByteBuffer buffer) {
        String methodName = "viewedBuffer";
        Method[] methods = buffer.getClass().getMethods();
        for (Method method : methods) {
            if (method.getName().equals("attachment")) {
                methodName = "attachment";
                break;
            }
        }

        ByteBuffer viewedBuffer = (ByteBuffer) invoke(buffer, methodName);
        if (viewedBuffer == null) {
            return buffer;
        } else {
            return viewed(viewedBuffer);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
//        MMapUtil mMapUtil = new MMapUtil();
        // 映射 1mb
//        mMapUtil.loadFileInMMap("E:\\bytewalk\\jproject\\eagle\\eaglemq\\broker\\store\\order_cancel_topic\\00000000", 0, 1024*1024*1);
        Scanner input = new Scanner(System.in);
        int i = 0;
        int size = input.nextInt();
        MMapUtil mMapUtil = new MMapUtil();
        String file = "E:\\bytewalk\\jproject\\eagle\\eaglemq\\broker\\store\\order_cancel_topic\\00000000";
        //默认是字节
        mMapUtil.loadFileInMMap(file, 0, 1024 * 1024 * size);
        System.out.println("映射了" + size + " m 的空间");
        TimeUnit.SECONDS.sleep(5);;
        mMapUtil.clean();
        System.out.println("释放内存");
        TimeUnit.SECONDS.sleep(10000);
    }
}
