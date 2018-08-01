package com.sharex.token.api.controller;

import com.sharex.token.api.entity.RESTful;
import com.sharex.token.api.service.CurrencyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api("交易所数据接口")
@RequestMapping("/currency")
@RestController
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @ApiOperation("单币聚合")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "exchangeName", value = "交易所", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "currency", value = "币种", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "klineType", value = "klineType", required = true)
    })
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public RESTful get(@RequestHeader String token, String exchangeName, String currency, String klineType) {

        return currencyService.get(token, exchangeName, currency, klineType);
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
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "exchangeName", value = "交易所", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "symbol", value = "列表symbol原样传入", required = true)
    })
    @RequestMapping(value = "/getTrades", method = RequestMethod.GET)
    public RESTful getTrades(String exchangeName, String symbol) {

        return currencyService.getTrades(exchangeName, symbol);
    }

    @ApiOperation("最新成交单向")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "exchangeName", value = "交易所", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "symbol", value = "列表symbol原样传入", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "direction", value = "buy/sell", required = true)
    })
    @RequestMapping(value = "/getTradesByDir", method = RequestMethod.GET)
    public RESTful getTradesByDir(String exchangeName, String symbol, String direction) {

        return currencyService.getTrades(exchangeName, symbol, direction);
    }
}
