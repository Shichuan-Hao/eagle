package cn.byteswalk.eaglemqconsole.vo;

import cn.byteswalk.eaglemqconsole.config.ResponseCodeEnum;

import java.util.Collections;
import java.util.List;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-06 13:38
 * @Description: PageResponseVO
 * @Version: 1.0
 */
public class PageResponseVO<T> {

    private int code;
    private String msg;
    private List<T> data;
    private int pageSize;
    private int currentPage;
    private int totalPage;

    public PageResponseVO() {}

    public PageResponseVO(
            int code, String msg, List<T> data,
            int pageSize, int currentPage, int totalPage) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.totalPage = totalPage;
    }

    public static <T> PageResponseVO<T> emptyPage() {
        return new PageResponseVO<T>()
                .setCode(ResponseCodeEnum.SUCCESS.getCode())
                .setMsg(ResponseCodeEnum.SUCCESS.getDesc())
                .setData(Collections.emptyList())
                .setCurrentPage(0)
                .setTotalPage(0)
                .setPageSize(0);
    }


    /**
     * 构建一个包含数据的成功分页响应对象。
     *
     * @param data 数据列表
     * @param totalPage 总页数
     * @param currentPage 当前页码
     * @param pageSize 每页大小
     * @param <T> 数据类型
     * @return 成功的分页响应对象
     */
    public static <T> PageResponseVO<T> success(List<T> data, int totalPage, int currentPage, int pageSize) {
        return new PageResponseVO<T>()
                .setCode(ResponseCodeEnum.SUCCESS.getCode())
                .setMsg(ResponseCodeEnum.SUCCESS.getDesc())
                .setData(data)
                .setCurrentPage(currentPage)
                .setTotalPage(totalPage)
                .setPageSize(pageSize);
    }

    public int getCode() {
        return code;
    }

    public PageResponseVO<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public PageResponseVO<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public List<T> getData() {
        return data;
    }

    public PageResponseVO<T> setData(List<T> data) {
        this.data = data;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public PageResponseVO<T> setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public PageResponseVO<T> setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        return this;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public PageResponseVO<T> setTotalPage(int totalPage) {
        this.totalPage = totalPage;
        return this;
    }

}

