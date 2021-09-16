package com.zlk.common.core.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description 重新封装返回实体
 * @Author wan
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/07/27
 **/

@Data
@ApiModel("响应体")
public class Response<T> implements Serializable {
    public static final String FAIL_CODE = "1111111";
    public static final String OK_CODE = "0000000";
    public static final Boolean OK = true;
    public static final Boolean FAIL = false;

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "是否成功")
    private boolean success = true;
    private Error error;
    @ApiModelProperty(value = "返回对象")
    protected T data;

    public Response() {
    }

    public static <T> Response<T> newFailResponse(String msg) {
        Response<T> r = new Response<>();
        r.setSuccess(FAIL);
        r.setError(new Error(FAIL_CODE, msg));
        return r;
    }

    public static <T> Response<T> newSuccessResponse(String msg,T t) {
        Response<T> r = new Response<>();
        r.setSuccess(OK);
        r.setData(t);
        r.setError(new Error(OK_CODE, msg));
        return r;
    }

    public static <T> Response<T> newSuccessResponse(T t) {
        Response<T> r = new Response();
        r.setSuccess(OK);
        r.setData(t);
        r.setError(new Response.Error("0000000", "成功"));
        return r;
    }

    @Data
    public static class Error implements Serializable {
        private static final long serialVersionUID = 1L;

        private String code;

        private String message;

        public Error() {}

        public Error(String code, String message) {
            this.code = code;
            this.message = message;
        }

    }
}
