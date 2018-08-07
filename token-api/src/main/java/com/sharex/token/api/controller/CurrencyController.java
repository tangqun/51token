package com.sharex.token.api.controller;

import com.sharex.token.api.entity.RESTful;
import com.sharex.token.api.entity.req.CurrencyPlaceOrder;
import com.sharex.token.api.entity.req.CurrencySynOrders;
import com.sharex.token.api.entity.req.ExchangeCurrencyCostEdit;
import com.sharex.token.api.service.CurrencyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

@Api("交易所数据接口")
@RequestMapping("/currency")
@RestController
@Validated
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
    public RESTful get(
            @RequestHeader String token, String exchangeName, String currency, String klineType) {

        return currencyService.get(token, exchangeName, currency, klineType);
    }

    @ApiOperation("同步委托订单")
    @RequestMapping(value = "/synOpenOrders", method = RequestMethod.POST)
    public RESTful synOpenOrders(
            @NotBlank(message = "token不能为空")
            @Pattern(regexp = "^[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}$", message = "token格式错误")
            @RequestHeader String token,
            @Valid
            @RequestBody CurrencySynOrders currencySynOrders) {

        return currencyService.synOpenOrders(token, currencySynOrders);
    }

    @ApiOperation("同步成交订单")
    @RequestMapping(value = "/synHistoryOrders", method = RequestMethod.POST)
    public RESTful synHistoryOrders(@RequestHeader String token, @RequestBody CurrencySynOrders currencySynOrders) {

        return currencyService.synHistoryOrders(token, currencySynOrders);
    }

    @ApiOperation("编辑交易所单币成本")
    @RequestMapping(value = "/editExchangeCurrencyCost", method = RequestMethod.POST)
    public RESTful editExchangeCurrencyCost(@RequestHeader String token, @RequestBody ExchangeCurrencyCostEdit exchangeCurrencyCostEdit) {

        return currencyService.editExchangeCurrencyCost(token, exchangeCurrencyCostEdit);
    }

    @ApiOperation("买单")
    @RequestMapping(value = "/buy", method = RequestMethod.POST)
    public RESTful buy(@RequestHeader String token, @RequestBody CurrencyPlaceOrder currencyPlaceOrder) {

        return currencyService.placeOrder(token, currencyPlaceOrder, "buy");
    }

    @ApiOperation("卖单")
    @RequestMapping(value = "/sell", method = RequestMethod.POST)
    public RESTful sell(@RequestHeader String token, @RequestBody CurrencyPlaceOrder currencyPlaceOrder) {

        return currencyService.placeOrder(token, currencyPlaceOrder, "sell");
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
