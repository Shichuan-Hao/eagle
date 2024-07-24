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

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * @Author hsc
 * @Date Create in 15:09 2024/7/23
 * @Description 支持基于 Java 的 MMap api 访问文件能力（文件的学写的能力）
 * <p>
 * 支持指定的 offset 的文件映射（结束映射的 offset-开始映射的 offset = 映射的内存体积） done!
 * 文件从指定的 offset 开始读取 done!
 * 文件从指定的 offset 开始写入 done!
 * 文件映射后的内存释放
 */
public class MMapUtil {

    private File file;
    private int mappedSize;
    private MappedByteBuffer mbb;
    private FileChannel fc;

    /**
     * 指定offset做文件映射
     *
     * @param filePath    文件路径
     * @param startOffset 开始映射的offset
     * @param mappedSize  映射的体积
     */
    public void loadFileInMMap(String filePath, int startOffset, int mappedSize)
            throws IOException {
        this.file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("filePath is " + filePath + " inValid");
        }
        this.fc = new RandomAccessFile(file, "rw").getChannel();
        this.mbb = this.fc.map(FileChannel.MapMode.READ_WRITE, startOffset, mappedSize);
        System.out.println("------");
    }

    /**
     * 支持从文件从指定的 offset 开始读取内容
     *
     * @param readOffset
     * @param size
     * @return
     */
    public byte[] readContent(int readOffset, int size) {
        mbb.position(readOffset);
        byte[] content = new byte[size];
        int j = 0;
        for (int i = 0; i < size; i++) {
            // 从内存里访问，快速高效，不用担心
            byte b = mbb.get(readOffset + i);
            content[j++] = b;
        }
        return content;
    }

    /**
     * 更高性能的一种写入api
     * @param content 内容
     */
    public void writeContent(byte[] content) {
        this.writeContent(content, false);
    }

    /**
     * 写入数据到磁盘中
     *
     * @param content 内容
     * @param force 强制刷盘标志
     */
    public void writeContent(byte[] content, boolean force) {
        // 默认刷到 page cache 中
        // 如果需要强制刷盘，这里要兼容
        mbb.put(content);
        if (force) {
            // 强制刷盘
            mbb.force();
        }
    }

    private void clear() throws IOException {
        mbb.clear();
        // 在关闭资源的时候执行以下代码释放内存
        // 不推荐的原因是因为使用了 sun包下不稳定的代码
//        Method method = FileChannelImpl.class.getDeclaredAnnotation("unmap", MappedByteBuffer.class);
//        method.setAccessible(true);
//        method.invoke(FileChannelImple.class, mbb);
        fc.close();
    }



    public void cleaner() {
        try {
            Method cleanerMethod = mbb.getClass().getMethod("cleaner");
            cleanerMethod.setAccessible(true);
            Object cleaner = cleanerMethod.invoke(mbb);
            Method cleanMethod = cleaner.getClass().getMethod("clean");
            cleanMethod.setAccessible(true);
            cleanMethod.invoke(cleaner);
        } catch (Exception e) {
            throw new RuntimeException("Failed to clean the MappedByteBuffer", e);
        }

    }


    public void clean() {
        if (mbb == null || !mbb.isDirect() || mbb.capacity() == 0) {
            return;
        }
        invoke(invoke(viewed(mbb), "cleaner"), "clean");
    }

    private Object invoke(final Object target, final String methodName, final Class<?>... args) {
        try {
            Method method = method(target, methodName, args);
            method.setAccessible(true);
            return method.invoke(target);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private Method method(Object target, String methodName, Class<?>... args) {
        try {
            return target.getClass().getMethod(methodName, args);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
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
        long size = input.nextLong();
        MMapUtil mMapUtil = new MMapUtil();
        //默认是字节
        mMapUtil.loadFileInMMap("E:\\bytewalk\\jproject\\eagle\\eaglemq\\broker\\store\\order_cancel_topic\\00000000", 0, (int) (1024 * 1024 * size));
        System.out.println("映射了" + size + "m的空间");
        TimeUnit.SECONDS.sleep(5);
        System.out.println("释放内存");
//        mMapUtil.clean();
        mMapUtil.cleaner();
        TimeUnit.SECONDS.sleep(10000);
    }

//    public void clean() {
//        if (mbb == null || !mbb.isDirect() || mbb.capacity() == 0) {
//            return;
//        }
//        invoke(invoke(viewed(mbb), "cleaner"), "clean");
//    }

// AccessController.doPrivileged 在 jdk17 已经过时
//    private Object invoke(final Object target, final  String methodName, final Class<?>... args) {
//        return AccessController.doPrivileged(new PrivilegedAction<Object>() {
//            @Override
//            public Object run() {
//
//                try {
//                    Method method = method(target, methodName, args);
//                    method.setAccessible(true);
//                    return method.invoke(target);
//                } catch (IllegalAccessException | InvocationTargetException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        });
//    }

//    private ByteBuffer viewed(ByteBuffer buffer) {
//        String methodName = "viewedBuffer";
//        Method[] methods = buffer.getClass().getMethods();
//        for (Method method : methods) {
//            if (method.getName().equals("attachment")) {
//                methodName = "attachment";
//                break;
//            }
//        }
//
//        ByteBuffer viewedBuffer = (ByteBuffer) invoke(buffer, methodName);
//        if (viewedBuffer == null) {
//            return buffer;
//        } else {
//            return viewed(viewedBuffer);
//        }
//    }
}
