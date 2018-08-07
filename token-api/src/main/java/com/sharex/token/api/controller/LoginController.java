package com.sharex.token.api.controller;

import com.sharex.token.api.entity.RESTful;
import com.sharex.token.api.entity.req.LoginPassword;
import com.sharex.token.api.entity.req.LoginSMSCode;
import com.sharex.token.api.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api("登陆")
@RestController
@Validated
public class LoginController {

    @Autowired
    private LoginService loginService;

    @ApiOperation("短信登陆")
    @RequestMapping(value = "/loginBySMSCode", method = RequestMethod.POST)
    public RESTful loginBySMSCode(
            @Valid
            @RequestBody LoginSMSCode loginSMSCode) {

        return loginService.loginBySMSCode(loginSMSCode);
    }

    @ApiOperation(("密码登陆"))
    @RequestMapping(value = "/loginByPassword", method = RequestMethod.POST)
    public RESTful loginByPassword(@RequestBody LoginPassword loginPassword) {

        return loginService.loginByPassword(loginPassword);
    }
}
