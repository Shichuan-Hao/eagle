package cn.byteswalk.eaglemq.common.coder;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-05 13:43
 * @Description: 网络传输时的数据格式，基于 Netty 的 RPC 传输的一个基本协议体
 * @Version: 1.0
 */
public class TcpMsg {

    /**
     * 魔数 4个字节
     * 安全校验参数
     * 当使用 Netty 解析到对象时校验该属性的值是否与预期一样，如果一样才会解析下闻的步骤
     */
    private short magic;

    /**
     * 请求包的绝体含义 4个字节
     * 根据 code 的值判断消息结构体是什么样的解析格式
     */
    private int code;

    /**
     * 代表整个消息体，即字节数组 body 的整体长度 4个字节
     * <p>
     * 将这个字节数组 `byte[] body` 长度 len 加载到本地内存并转换成 byte 数组之后，就可以根据 code 值得出消息体要解析成什么样的一个
     * Java 对象，而后将这个 Java 对象转换成 JSON 格式
     */
    private int len;

    /**
     * 消息体
     */
    private byte[] body;

    public TcpMsg() {}

    public TcpMsg(int code, byte[] body) {
        this.code = code;
        this.body = body;
    }

    public short getMagic() {
        return magic;
    }

    public void setMagic(short magic) {
        this.magic = magic;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
}

