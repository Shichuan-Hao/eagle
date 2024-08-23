package cn.byteswalk.eaglemqbroker.core;

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

/**
 * 最基础的 mmap 对象模型
 */
public class MMapFileModel {

    private static final String RW_ACCESS_MODE = "rw";

    private File file;
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
        this.fileChannel = new RandomAccessFile(file, RW_ACCESS_MODE).getChannel();
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
        // 定位到最新的 commitLog 文件中，记录下当前文件是否已经写满，如果写满，则创建新的文件，并且做新的 mmap 映射
        // 如果当前文件没有写满，对content内容做一层封装(我们不可能直接把原始数据写进去，它还会有一些附属信息)，再判断写入是否会导致commitLog写满，如果不会，就选择当前 commitLog，如果写满，则创建新的文件，并且做新的 mmap 映射
        ByteBuffer byteBuffer = mappedByteBuffer.slice();
        byteBuffer.position(1111);
        byteBuffer.put(content);

        // 默认刷到 page cache 中
        // 如果需要强制刷盘，这里要兼容
        mappedByteBuffer.put(content);
        if (force) {
            // 强制刷盘
            mappedByteBuffer.force();
        }
    }

    /**
     * 释放mmap内存
     */
    public void clean() {
        if (mappedByteBuffer == null || !mappedByteBuffer.isDirect() || mappedByteBuffer.capacity() == 0) {
            return;
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

}
