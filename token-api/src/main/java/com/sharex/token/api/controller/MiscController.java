package com.sharex.token.api.controller;

import com.sharex.token.api.entity.RESTful;
import com.sharex.token.api.entity.req.MiscRevAppEx;
import com.sharex.token.api.service.MiscService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api("杂项")
@RequestMapping("/misc")
@RestController
public class MiscController {

    @Autowired
    private MiscService miscService;

    @ApiOperation("异常收集")
    @RequestMapping(value = "/revAppEx", method = RequestMethod.POST)
    public RESTful revAppEx(@RequestBody MiscRevAppEx miscRevAppEx) {

        return miscService.revAppEx(miscRevAppEx);
    }

    @ApiOperation("获取App最新版本号")
    @RequestMapping(value = "/getAppVersion", method = RequestMethod.GET)
    public RESTful getAppVersion() {

        return miscService.getAppVersion();
    }

    @ApiOperation("韭菜语录")
    @RequestMapping(value = "/getQuotation", method = RequestMethod.GET)
    public RESTful getQuotation() {

        return miscService.getQuotation();
    }
}
