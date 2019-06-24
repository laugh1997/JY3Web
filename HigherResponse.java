package com.neuedu.commen;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 封装可复用的返回对象
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class HigherResponse<T> {

    private HigherResponse()
    {

    }

    private HigherResponse(Integer status) {
        this.status = status;
    }

    private HigherResponse(Integer status,T t)
    {
        this.status = status;
        this.data = t;
    }

    private HigherResponse(Integer status,String msg)
    {
        this.status = status;
        this.msg = msg;
    }

    private HigherResponse(Integer status,String msg,T t)
    {
        this.status = status;
        this.msg = msg;
        this.data = t;
    }


    private Integer status;

    private T data;

    private String msg;




//提供对外空开的方法

//判断是否成功
@JsonIgnore
 public boolean isResponseSuccess()
 {
     return this.status == StatusUtil.SUCCESSSTATUS;
 }

//成功只返回状态 1
    public static HigherResponse getResponseSuccess()
    {
        return new HigherResponse(StatusUtil.SUCCESSSTATUS);
    }

//成功返回 状态1 + 信息
    public static HigherResponse getResponseSuccess(String msg)
    {
        return new HigherResponse(StatusUtil.SUCCESSSTATUS,msg);
    }

//成功返回 状态1 + 数据
    public static <T> HigherResponse getResopnseSuccess(T t)
    {
        return new HigherResponse(StatusUtil.SUCCESSSTATUS,t);
    }

//成功返回 状态1 + 数据 + 信息
    public static <T> HigherResponse getResponseSuccess(String msg,T t)
    {
        return new HigherResponse(StatusUtil.SUCCESSSTATUS,msg,t);
    }


//判断是否失败
@JsonIgnore
    public boolean isResponseFail()
    {
        return this.status == StatusUtil.FAILEDSTATUS;
    }

//失败返回 状态码 0 + 信息
    public static HigherResponse getResponseFailed(String msg)
    {
        return new HigherResponse(StatusUtil.FAILEDSTATUS,msg);
    }

//失败返回状态码 0
    public static HigherResponse getResponseFailed()
    {
        return new HigherResponse(StatusUtil.FAILEDSTATUS);
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
