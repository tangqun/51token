package com.sharex.token.api.controller;

import com.sharex.token.api.entity.RESTful;
import com.sharex.token.api.service.HuoBiService;
import com.sharex.token.api.service.OkexService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api("心跳")
@RestController
public class HomeController {

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {

        return "index";
    }

    @Autowired
    private OkexService okexService;

    @ApiOperation("获取用户信息")
    @RequestMapping(value = "/getUserinfo", method = RequestMethod.GET)
    public RESTful getUserinfo() {

        return okexService.getUserinfo();
    }

    @Autowired
    private HuoBiService huoBiService;

    @ApiOperation("Symbols")
    @RequestMapping(value = "/getSymbols", method = RequestMethod.GET)
    public RESTful getSymbols() {

        return huoBiService.getSymbols();
    }

    @ApiOperation("账户信息")
    @RequestMapping(value = "/getAccounts", method = RequestMethod.GET)
    public RESTful getAccounts() {

        return huoBiService.getAccounts();
    }
}
