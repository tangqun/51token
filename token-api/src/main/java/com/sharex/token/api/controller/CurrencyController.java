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

    @ApiOperation("单币聚合")
    @RequestMapping(value = "/get/{token}", method = RequestMethod.GET)
    public RESTful get(String token, String exchangeName, String currency) {

        return currencyService.get(token, exchangeName, currency);
    }

    @ApiOperation("行情")
    @RequestMapping(value = "/getTicker", method = RequestMethod.GET)
    public RESTful getTicker(String exchangeName, String symbol) {

        return currencyService.getTicker(exchangeName, symbol);
    }

    @ApiOperation("k线")
    @RequestMapping(value = "/getKline", method = RequestMethod.GET)
    public RESTful getKline(String exchangeName, String symbol, String type) {

        return currencyService.getKline(exchangeName, symbol, type);
    }

    @ApiOperation("最新成交")
    @RequestMapping(value = "/getTrades", method = RequestMethod.GET)
    public RESTful getTrades(String exchangeName, String symbol, String direction) {

        return currencyService.getTrades(exchangeName, symbol, direction);
    }
}
