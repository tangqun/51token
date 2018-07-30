package com.sharex.token.api.controller;

import com.sharex.token.api.entity.RESTful;
import com.sharex.token.api.entity.req.Feedback;
import com.sharex.token.api.entity.req.SwitchKline;
import com.sharex.token.api.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation("用户反馈")
    @RequestMapping(value = "/feedback", method = RequestMethod.POST)
    public RESTful feedback(@RequestHeader String token, @RequestBody Feedback feedback) {

        return userService.feedback(token, feedback);
    }

    @ApiOperation("切换kline红绿显示")
    @RequestMapping(value = "/switchKline", method = RequestMethod.POST)
    public RESTful switchKline(@RequestHeader String token, SwitchKline switchKline) {

        return userService.switchKline(token, switchKline);
    }
}
