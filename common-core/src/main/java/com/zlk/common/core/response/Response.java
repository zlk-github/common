package com.zlk.common.core.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author likuan.zhou
 * @title: BeanExUtils
 * @projectName common
 * @description: 封装返回实体
 * @date 2021/9/14/014 19:56
 */
@Data
@ApiModel("响应体")
public class Response<T> implements Serializable {
    public static final String SYSTEM_FAIL_CODE = "500";
    public static final String OK_CODE = "0";

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "0成功，非0错误描述")
    private String code;
    @ApiModelProperty(value = "描述（code为非0为错误描述）")
    private String msg;
    @ApiModelProperty(value = "返回对象")
    protected T data;

    public Response() {
    }

    /**
     * 只返回失败描述,系统异常
     * @param <T>
     * @return
     */
    public static <T> Response<T> newFailResponse() {
        Response<T> r = new Response<>();
        r.setCode(SYSTEM_FAIL_CODE);
        r.setMsg("系统开小差中，请稍后再试...");
        return r;
    }

    /**
     * 自定义code,返回描述。业务异常
     * @param code
     * @param <T>
     * @return
     */
    public static <T> Response<T> newSuccessResponse(String code,String msg) {
        Response<T> r = new Response<>();
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }

    /**
     * 自定义code,返回描述与数据。业务异常
     * @param code
     * @param t
     * @param <T>
     * @return
     */
    public static <T> Response<T> newSuccessResponse(String code,String msg,T t) {
        Response<T> r = new Response<>();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(t);
        return r;
    }

    /**
     * 成功,并带描述与数据
     * @param t
     * @param <T>
     * @return
     */
    public static <T> Response<T> newSuccessResponse(T t) {
        Response<T> r = new Response<>();
        r.setCode(SYSTEM_FAIL_CODE);
        r.setData(t);
        return r;
    }

    /**
     * 成功，带数据，无描述
     * @param msg
     * @param t
     * @param <T>
     * @return
     */
    public static <T> Response<T> newSuccessResponse(String msg,T t) {
        Response<T> r = new Response<>();
        r.setCode(SYSTEM_FAIL_CODE);
        // 接口成功，但是需要部分前端提示
        r.setMsg(msg);
        r.setData(t);
        return r;
    }

}
