package com.sharex.token.api.controller;

import com.sharex.token.api.entity.RESTful;
import com.sharex.token.api.entity.req.PasswordLogin;
import com.sharex.token.api.entity.req.SMSCodeLogin;
import com.sharex.token.api.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api("登陆")
@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @ApiOperation("短信登陆")
    @RequestMapping(value = "/loginbysmscode", method = RequestMethod.POST)
    public RESTful loginBySMSCode(@RequestBody SMSCodeLogin smsCodeLogin) {

        return userService.loginBySMSCode(smsCodeLogin);
    }

    @RequestMapping(value = "/loginbypassword", method = RequestMethod.POST)
    public RESTful loginByPassword(@RequestBody PasswordLogin passwordLogin) {

        return userService.loginByPassword(passwordLogin);
    }
}
