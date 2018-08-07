package com.sharex.token.api.entity.req;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class LoginForm {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Length(min = 6, message = "密码长度至少6位")
    private String password;

    @NotBlank(message = "验证码不能为空")
    @Length(min = 6, max = 6, message = "验证码长度为6位")
    private String code;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
