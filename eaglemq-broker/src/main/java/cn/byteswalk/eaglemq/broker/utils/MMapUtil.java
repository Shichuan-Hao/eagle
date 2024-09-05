package cn.byteswalk.eaglemq.utils;


import cn.byteswalk.eaglemq.common.constants.CommonConstants;

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
import java.util.concurrent.CountDownLatch;

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
    // gc管理呢？否定的
    /**
     * MappedByteBuffer 是一个 DirectByteBuffer 直接内存块，它的特点是不受限于 JVM 的 GC 垃圾回收的管理，这也就意味这
     * 当将这个对象设置为 null 时不代表该对象会自动被GC识别回收最后释放掉其所占用的内存块，它所关联的内存块时比较特别的，属于 native memory，
     * 因此，对 MappedByteBuffer 的内存释放是必须要考虑的
     */
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
        if (fileChannel != null) {
            try {
                fileChannel.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        invoke(invoke(viewed(mappedByteBuffer), "cleaner"), "clean");
    }

    // AccessController.doPrivileged 在 jdk17 已经过时
    private Object invoke(final Object target, final String methodName, final Class<?>... args) {
        return AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
            try {
                Method method = method(target, methodName, args);
                method.setAccessible(true);
                return method.invoke(target);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
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
        MMapUtil mMapUtil = new MMapUtil();
        //映射1mb
        mMapUtil.loadFileInMMap(CommonConstants.TEST_PATH, 0, 1024 * 1024);
        CountDownLatch count = new CountDownLatch(1);
        CountDownLatch allWriteSuccess = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            Thread task = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        count.await();
                        //多线程并发写
                        mMapUtil.writeContent(("test-content-" + finalI).getBytes());
                        allWriteSuccess.countDown();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            task.start();
        }
        System.out.println("准备执行并发写入mmap测试");
        count.countDown();
        allWriteSuccess.await();
        System.out.println("并发测试完毕,读取文件内容测试");
        byte[] content = mMapUtil.readContent(0, 20000);
        System.out.println("内容：" + new String(content));
    }
}
