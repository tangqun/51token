package com.sharex.token.api.controller;

import com.sharex.token.api.entity.RESTful;
import com.sharex.token.api.entity.req.Feedback;
import com.sharex.token.api.entity.req.SwitchKline;
import com.sharex.token.api.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

@RequestMapping("/user")
@RestController
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation("用户反馈")
    @RequestMapping(value = "/feedback", method = RequestMethod.POST)
    public RESTful feedback(
            @NotBlank(message = "token不能为空")
            @Pattern(regexp = "^[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}$", message = "token格式错误")
            @RequestHeader String token,
            @Valid
            @RequestBody Feedback feedback) {

        return userService.feedback(token, feedback);
    }

    @ApiOperation("切换kline红绿显示")
    @RequestMapping(value = "/switchKline", method = RequestMethod.POST)
    public RESTful switchKline(
            @NotBlank(message = "token不能为空")
            @Pattern(regexp = "^[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}$", message = "token格式错误")
            @RequestHeader String token,
            @Valid
            @RequestBody SwitchKline switchKline) {

        return userService.switchKline(token, switchKline);
    }
}
