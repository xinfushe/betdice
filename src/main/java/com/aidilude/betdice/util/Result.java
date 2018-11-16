package com.aidilude.betdice.util;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class Result {

    //###########################################属性###########################################

    private Integer code;   //状态码

    private String msg;

    private Object data;

    private Integer totalCount;   //总记录数

    private Integer pageSize;   //每页大小

    private Integer pageCount;   //页数

    //###########################################getter、setter###########################################

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) { this.code = code; }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    //###########################################构造器###########################################

    public Result(ResultCode code, String msg) {
        this.code = code.getValue();
        this.msg = msg;
    }

    public Result(ResultCode code, Object data) {
        this.code = code.getValue();
        this.data = data;
    }

    public Result(ResultCode code, Object data, Integer totalCount, Integer pageSize, Integer pageCount) {
        this.code = code.getValue();
        this.data = data;
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.pageCount = pageCount;
    }

    //###########################################功能函数###########################################

    /**
     * 返回正确消息
     * @param msg
     * @return
     */
    public static Result ok(String msg) {
        return new Result(ResultCode.Ok, msg);
    }

    /**
     * 返回错误消息
     * @param msg
     * @return
     */
    public static Result error(String msg) {
        return new Result(ResultCode.Error, msg);
    }

    /**
     * 根据状态码返回消息
     * @param code
     * @param msg
     * @return
     */
    public static Result returnMsg(ResultCode code, String msg){
        return new Result(code, msg);
    }

    /**
     * 返回单个数据
     * @param data
     * @return
     */
    public static Result returnSingleData(Object data) {
        return new Result(ResultCode.Ok, data);
    }

    /**
     * 返回分页数据
     * @param data
     * @param totalCount
     * @param pageSize
     * @param pageCount
     * @return
     */
    public static Result returnPagingData(Object data, Integer totalCount, Integer pageSize, Integer pageCount) {
        return new Result(ResultCode.Ok, data, totalCount, pageSize, pageCount);
    }

    /**
     * 拦截器专用消息返回
     * @param response
     * @param code
     * @param msg
     * @throws IOException
     */
    public static void returnMsg(HttpServletResponse response, ResultCode code, String msg) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter pw = response.getWriter();
        Result result = new Result(code, msg);
        String resultJson = JSONObject.toJSONString(result);
        pw.write(resultJson);
    }

    public static void main(String[] args) {

    }

}