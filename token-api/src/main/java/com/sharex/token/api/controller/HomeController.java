package com.sharex.token.api.controller;

import com.sharex.token.api.util.HttpUtil;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api("心跳")
@RestController
public class HomeController {

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {

        return HttpUtil.get("https://api.huobipro.com/v1/common/timestamp");
    }
}
