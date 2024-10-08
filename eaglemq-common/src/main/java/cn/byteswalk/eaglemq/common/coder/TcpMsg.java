package cn.byteswalk.eaglemq.common.coder;

import cn.byteswalk.eaglemq.common.constants.BrokerConstants;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-05 13:43
 * @Description: 网络传输时的数据格式，基于 Netty 的 RPC 传输的一个基本协议体
 * @Version: 1.0
 */
public class TcpMsg {
    /**
     * 魔数
     */
    private short magic;
    /**
     * 表示请求包的具体含义
     */
    private int code;
    /**
     * 消息长度
     */
    private int len;
    /**
     * 消息内容
     */
    private byte[] body;

    public TcpMsg(int code, byte[] body) {
        this.magic = BrokerConstants.DEFAULT_MAGIC_NUM;
        this.code = code;
        this.body = body;
        this.len = body.length;
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
