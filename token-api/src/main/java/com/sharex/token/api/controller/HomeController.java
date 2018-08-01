package com.sharex.token.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api("心跳")
@RestController
public class HomeController {

    @ApiOperation("心跳脉冲")
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {

        return "1";
    }
}
