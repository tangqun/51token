package com.sharex.token.api.controller;

import com.sharex.token.api.entity.RESTful;
import com.sharex.token.api.service.CurrencyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api("交易所数据接口")
@RequestMapping("/currency")
@RestController
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @ApiOperation("行情")
    @RequestMapping(value = "/getTicker", method = RequestMethod.GET)
    public RESTful getTicker(String exchangeName, String symbol) {

        return currencyService.getTicker(exchangeName, symbol);
    }
}
