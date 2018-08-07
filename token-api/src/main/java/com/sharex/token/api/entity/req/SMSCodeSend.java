package com.sharex.token.api.entity.req;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

public class SMSCodeSend {

    @NotBlank(message = "手机号不能位空")
    @Pattern(regexp = "^1[3|4|5|7|8][0-9]\\d{4,8}$", message = "手机号格式错误")
    private String mobileNum;

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }
}
