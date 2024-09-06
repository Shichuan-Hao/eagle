package cn.byteswalk.eaglemqconsole.vo;

import cn.byteswalk.eaglemqconsole.config.ResponseCodeEnum;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-03 18:04
 * @Description:
 * @Version: 1.0
 */
public class BaseResponseVO<T> {

    private int code;
    private String msg;
    private T data;

    // 无参构造函数，默认成功响应
    public BaseResponseVO() {
        this(ResponseCodeEnum.SUCCESS.getCode(), ResponseCodeEnum.SUCCESS.getDesc(), null);
    }

    // 参数构造函数
    public BaseResponseVO(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // 静态泛型方法，返回带数据的成功响应
    public static <T> BaseResponseVO<T> success(T data) {
        return new BaseResponseVO<>(ResponseCodeEnum.SUCCESS.getCode(), ResponseCodeEnum.SUCCESS.getDesc(), data);
    }

    // 静态方法，返回不带数据的成功响应
    public static <T> BaseResponseVO<T> success() {
        return new BaseResponseVO<>(ResponseCodeEnum.SUCCESS.getCode(), ResponseCodeEnum.SUCCESS.getDesc(), null);
    }

    // Getter 和 Setter 方法
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}


