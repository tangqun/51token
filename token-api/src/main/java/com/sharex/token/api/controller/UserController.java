package com.sharex.token.api.controller;

import com.sharex.token.api.entity.RESTful;
import com.sharex.token.api.entity.req.Feedback;
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
    @RequestMapping(value = "/feedback/{token}", method = RequestMethod.POST)
    public RESTful feedback(@PathVariable String token, @RequestBody Feedback feedback) {

        return userService.feedback(token, feedback);
    }
}
