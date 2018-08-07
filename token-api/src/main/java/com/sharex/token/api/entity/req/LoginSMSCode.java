package com.sharex.token.api.entity.req;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

public class LoginSMSCode {

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3|4|5|7|8][0-9]\\d{4,8}$", message = "手机号格式错误")
    private String mobileNum;
    @NotBlank(message = "验证码不能为空")
    @Pattern(regexp = "^\\d{6}$", message = "验证码为6位数字")
    private String smsCode;

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }
}
