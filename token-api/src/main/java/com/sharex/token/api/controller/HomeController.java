package com.sharex.token.api.controller;

import io.swagger.annotations.Api;
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
}
