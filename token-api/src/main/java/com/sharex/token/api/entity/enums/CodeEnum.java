package com.sharex.token.api.entity.enums;

/**
 * Created by TQ on 2017/11/15.
 * 除了不能继承，基本上可以将enum 看做一个常规的类
 */
public enum CodeEnum {
    SystemException(-1, "系统异常"),
    Success(0, "成功"),

    MobileNumCannotBeNull(120000, "手机号不能为空"),
    MobileNumFormatError(120001, "手机号格式错误"),
    SMSCodeCannotBeNull(120002, "短信验证码不能为空"),
    SMSCodeFormaError(120003, "短信验证码格式错误"),

    SMSCodeNotInDB(120004, "请先获取短信验证码"),
    SMSCodeHasBeenUsed(120005, "请重新获取短信验证码"),
    SMSCodeNotEqualsInDB(120006, "短信验证码不正确"),

    AccountHasBeenFrozen(120007, "账户被冻结，请联系客服"),
    SMSSendFail(120008, "短信发送失败，请稍后重试"),
    SMSOutOfLimit(120009, "短信条数限制"),
    UserNotInDB(120010, "用户名密码错误"),
    PasswordNotEqualsInDB(120011, "用户名密码错误")
    ;

    CodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private int code;
    private String msg;

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
}
