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

    public static <T> BaseResponseVO<T> success(Object data) {
        BaseResponseVO responseVO = new BaseResponseVO();
        responseVO.setCode(ResponseCodeEnum.SUCCESS.getCode());
        responseVO.setData(data);
        responseVO.setMsg(ResponseCodeEnum.SUCCESS.getDesc());
        return responseVO;
    }

    public static <T> BaseResponseVO success() {
        return new BaseResponseVO();
    }

    public BaseResponseVO() {
    }

    public BaseResponseVO(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

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

