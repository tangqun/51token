package com.sharex.token.api.entity.enums;

/**
 * Created by TQ on 2017/11/15.
 * 除了不能继承，基本上可以将enum 看做一个常规的类
 */
public enum CodeEnum {
    SystemException(-1, "系统异常"),
    Success(0, "成功"),


    AppIdError(401, "AppId错误"),
    AuthorizedTypeIdError(402, "AuthorizedTypeId错误"),
    IPCannotBeNULLOrEmpty(403, "IP不能为空"),
    IPFormatError(404, "IP格式错误"),
    SystemTypeIdError(405, "SystemTypeId错误"),
    EquipmentNumFormatError(406, "EquipmentNum格式错误"),
    OpenIdCannotBeNULLOrEmpty(407, "OpenId不能为空"),
    UnionIdCannotBeNULLOrEmpty(408, "UnionId不能为空"),
    ServerRefuseThisIPRequest(409, "服务器拒绝此IP请求"),
    UnionUserIdCannotBeNULLOrEmpty(410, "UnionUserId不能为空"),
    UnionUserIdFormatError(411, "UnionUserId格式错误"),

    MobileNumCannotNULLOrEmpty(101, "MobileNum不能为空"),
    MobileNumFormatError(102, "MobileNum格式错误"),
    PasswordCannotBeNULLOrEmpty(103, "Password不能为空"),
    PasswordFormatError(104, "Password格式错误"),


    NewMobileNumCannotNULLOrEmpty(105, "NewMobileNum不能为空"),
    NewMobileNumFormatError(106, "NewMobileNum格式错误"),


    OldPasswordCannotBeNULLOrEmpty(107, "OldPassword不能为空"),
    OldPasswordFormatError(108, "OldPassword格式错误"),
    NewPasswordCannotBeNULLOrEmpty(109, "NewPassword不能为空"),
    NewPasswordFormatError(110, "NewPassword格式错误"),


    TokenCannotBeNULLOrEmpty(111, "Token不能为空"),
    TokenFormatError(112, "Token格式错误"),
    SMSCodeCannotBeNULLOrEmpty(113, "短信验证码不能为空"),
    SMSCodeFormatError(114, "短信验证码格式错误"),


    AnonymousLoginAppIdError(801, "匿名登陆AppId错误"),
    AnonymousLoginIPCannotBeNULLOrEmpty(802, "匿名登陆IP不能为空"),
    AnonymousLoginIPFormatError(803, "匿名登陆IP格式错误"),
    AnonymousLoginSystemTypeIdError(804, "匿名登陆SystemTypeId错误"),
    AnonymousLoginEquipmentNumFormatError(806, "匿名登陆EquipmentNum格式错误"),


    TokenInvalid(1001, "Token无效"),
    SMSCodeInvalid(1002, "SMSCode无效"),
    MobileNumHasBeenRegistered(1003, "MobileNum已被注册"),
    MobileNumHasNotBeenRegistered(1004, "MobileNum尚未注册"),
    PasswordInvalid(1005, "Password无效"),
    AccountHasBeenFrozen(1006, "账号已被冻结"),
    WeChatHasBeenBoundToMobileNum(1007, "三方账号已绑定手机号"),
    AppVersionTooLow(1008, "App版本过低"),
    AppVersionDoesNotSupportBindingMobileNum(1010, "当前App版本不支持绑定手机号"),

    UpdateUnionUserIdFail(1011, "更新UnionUserId失败"),

    WeChatUserInfoConflictWithAppId(1013, "三方用户信息与AppId冲突"),
    UnionUserIdInvalid(1014, "UnionUserId无效");

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
