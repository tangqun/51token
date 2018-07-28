package com.sharex.token.admin.entity;


import com.sharex.token.admin.entity.enums.CodeEnum;

/**
 * Created by TQ on 2017/11/15.
 */
public class RESTful {
    private Integer code;
    private String msg;
    private Integer count;
    private Object data;

    public static RESTful Success() {
        return Success(null);
    }

    public static RESTful Success(Object data) {
        return Success(0, data);
    }

    public static RESTful Success(Integer count, Object data) {
        return Success(CodeEnum.Success, count, data);
    }

    private static RESTful Success(CodeEnum codeEnum, Integer count, Object data) {
        return new RESTful(codeEnum.getCode(), codeEnum.getMsg(), count, data);
    }

    public static RESTful SystemException() {
        return Fail(CodeEnum.SystemException);
    }

    public static RESTful Fail(CodeEnum codeEnum) {
        return Fail(codeEnum, 0, null);
    }

    private static RESTful Fail(CodeEnum codeEnum, Integer count, Object data) {
        return new RESTful(codeEnum.getCode(), codeEnum.getMsg(), count, data);
    }

    private RESTful(Integer code, String msg, Integer count, Object data)
    {
        this.code = code;
        this.msg = msg;
        this.count = count;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
