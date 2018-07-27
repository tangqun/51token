package com.sharex.token.api.entity.enums;

/**
 * Created by TQ on 2017/11/15.
 * 除了不能继承，基本上可以将enum 看做一个常规的类
 */
public enum CodeEnum {
    SystemException(-1, "系统异常"),
    Success(0, "成功"),

    // 需要特殊处理code
    TokenInvalid(100000, "请重新登陆"),
    AccountHasBeenFrozen(100001, "账户被冻结，请联系客服"),

    MobileNumCannotBeNull(120000, "手机号不能为空"),
    MobileNumFormatError(120001, "手机号格式错误"),
    SMSCodeCannotBeNull(120002, "短信验证码不能为空"),
    SMSCodeFormaError(120003, "短信验证码格式错误"),

    SMSCodeNotInDB(120004, "请先获取短信验证码"),
    SMSCodeHasBeenUsed(120005, "请重新获取短信验证码"),
    SMSCodeNotEqualsInDB(120006, "短信验证码不正确"),

    SMSSendFail(120008, "短信发送失败，请稍后重试"),
    SMSOutOfLimit(120009, "短信条数限制"),
    UserNotInDB(120010, "用户名密码错误"),
    PasswordNotEqualsInDB(120011, "用户名密码错误"),

    TokenCannotBeNull(120012, "token不能为空"),
    TokenFormatError(120013, "token格式错误"),

    ApiKeyCannotBeNull(120014, "ApiKey不能为空"),
    ApiKeyFormatError(120015, "ApiKey格式错误"),

    ApiSecretCannotBeNull(120016, "ApiSecret不能为空"),
    ApiSecretFormatError(120017, "ApiSecret格式错误"),

    RepeatAuthOfAsset(120018, "已授权成功，请勿重复提交"),
    RepeatRmAuthOfAsset(120019, "授权已取消，请勿重复提交"),

    ExchangeInvalid(120020, "交易所无效"),
    ExchangeInDBNotConfig(120021, "交易所未配置"),

    NotExistAuthOfExchange(120022, "该交易所未授权"),

    AssetNotSyn(120023, "资产未同步"),

    FeedbackContentCannotBeNull(120024, "反馈内容不能未空"),
    FeedbackContentFormatError(120025, "反馈内容格式错误"),
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
