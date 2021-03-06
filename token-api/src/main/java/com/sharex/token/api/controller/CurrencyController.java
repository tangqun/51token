package com.sharex.token.api.controller;

import com.sharex.token.api.entity.RESTful;
import com.sharex.token.api.entity.req.*;
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
            @NotBlank(message = "token不能为空")
            @Pattern(regexp = "^[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}$", message = "token格式错误")
            @RequestHeader String token, String exchangeName, String currency, String klineType) {

        return currencyService.get(token, exchangeName, currency, klineType);
    }

    @ApiOperation("买/卖--页面数据")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "exchangeName", value = "交易所", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "currency", value = "币种", required = true)
    })
    @RequestMapping(value = "/getTrades", method = RequestMethod.GET)
    public RESTful getTrades(
            @NotBlank(message = "token不能为空")
            @Pattern(regexp = "^[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}$", message = "token格式错误")
            @RequestHeader String token, String exchangeName, String currency) {

        return currencyService.getTrades(token, exchangeName, currency);
    }

    @ApiOperation("同步委托订单--交易所")
    @RequestMapping(value = "/synExchangeOpenOrders", method = RequestMethod.POST)
    public RESTful synExchangeOpenOrders(
            @NotBlank(message = "token不能为空")
            @Pattern(regexp = "^[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}$", message = "token格式错误")
            @RequestHeader String token,
            @Valid
            @RequestBody ExchangeOpenOrdersSyn exchangeOpenOrdersSyn) {

        return currencyService.synExchangeOpenOrders(token, exchangeOpenOrdersSyn);
    }

    @ApiOperation("同步委托订单")
    @RequestMapping(value = "/synCurrencyOpenOrders", method = RequestMethod.POST)
    public RESTful synCurrencyOpenOrders(
            @NotBlank(message = "token不能为空")
            @Pattern(regexp = "^[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}$", message = "token格式错误")
            @RequestHeader String token,
            @Valid
            @RequestBody CurrencySynOrders currencySynOrders) {

        return currencyService.synCurrencyOpenOrders(token, currencySynOrders);
    }

    @ApiOperation("同步成交订单--交易所")
    @RequestMapping(value = "/synExchangeHistoryOrders", method = RequestMethod.POST)
    public RESTful synExchangeHistoryOrders(
            @NotBlank(message = "token不能为空")
            @Pattern(regexp = "^[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}$", message = "token格式错误")
            @RequestHeader String token,
            @Valid
            @RequestBody ExchangeHistoryOrdersSyn exchangeHistoryOrdersSyn) {

        return currencyService.synExchangeHistoryOrders(token, exchangeHistoryOrdersSyn);
    }

    @ApiOperation("同步成交订单")
    @RequestMapping(value = "/synCurrencyHistoryOrders", method = RequestMethod.POST)
    public RESTful synCurrencyHistoryOrders(
            @NotBlank(message = "token不能为空")
            @Pattern(regexp = "^[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}$", message = "token格式错误")
            @RequestHeader String token,
            @Valid
            @RequestBody CurrencySynOrders currencySynOrders) {

        return currencyService.synCurrencyHistoryOrders(token, currencySynOrders);
    }

    @ApiOperation("编辑交易所单币成本")
    @RequestMapping(value = "/editExchangeCurrencyCost", method = RequestMethod.POST)
    public RESTful editExchangeCurrencyCost(
            @NotBlank(message = "token不能为空")
            @Pattern(regexp = "^[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}$", message = "token格式错误")
            @RequestHeader String token,
            @Valid
            @RequestBody ExchangeCurrencyCostEdit exchangeCurrencyCostEdit) {

        return currencyService.editExchangeCurrencyCost(token, exchangeCurrencyCostEdit);
    }

    @ApiOperation("买单")
    @RequestMapping(value = "/buy", method = RequestMethod.POST)
    public RESTful buy(
            @NotBlank(message = "token不能为空")
            @Pattern(regexp = "^[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}$", message = "token格式错误")
            @RequestHeader String token,

            @RequestBody CurrencyPlaceOrder currencyPlaceOrder) {

        return currencyService.placeOrder(token, currencyPlaceOrder, "buy");
    }

    @ApiOperation("卖单")
    @RequestMapping(value = "/sell", method = RequestMethod.POST)
    public RESTful sell(
            @NotBlank(message = "token不能为空")
            @Pattern(regexp = "^[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}$", message = "token格式错误")
            @RequestHeader String token,

            @RequestBody CurrencyPlaceOrder currencyPlaceOrder) {

        return currencyService.placeOrder(token, currencyPlaceOrder, "sell");
    }

    @ApiOperation("撤销委托")
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public RESTful cancel(
            @NotBlank(message = "token不能为空")
            @Pattern(regexp = "^[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}$", message = "token格式错误")
            @RequestHeader String token, @RequestBody CurrencyCancelOrder currencyCancelOrder) {

        return currencyService.cancelOrder(token, currencyCancelOrder);
    }

    @ApiOperation("获取历史委托")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "exchangeName", value = "交易所", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "currency", value = "币种", required = true)
    })
    @RequestMapping(value = "/getOpenOrders", method = RequestMethod.GET)
    public RESTful getOpenOrders(
            @NotBlank(message = "token不能为空")
            @Pattern(regexp = "^[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}$", message = "token格式错误")
            @RequestHeader String token, String exchangeName, String currency) {

        return currencyService.getOpenOrders(token, exchangeName, currency);
    }

    @ApiOperation("获取历史成交")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "exchangeName", value = "交易所", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "currency", value = "币种", required = true)
    })
    @RequestMapping(value = "/getHistoryOrders", method = RequestMethod.GET)
    public RESTful getHistoryOrders(
            @NotBlank(message = "token不能为空")
            @Pattern(regexp = "^[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}$", message = "token格式错误")
            @RequestHeader String token, String exchangeName, String currency) {

        return currencyService.getHistoryOrders(token, exchangeName, currency);
    }

    @ApiOperation("测试接口--买卖盘无用户")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "exchangeName", value = "交易所", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "currency", value = "币种", required = true)
    })
    @RequestMapping(value = "/testGetTrades", method = RequestMethod.GET)
    public RESTful testGetTrades(String exchangeName, String currency) {

        return currencyService.testGetTrades(exchangeName, currency);
    }

    @ApiOperation("测试接口--k线")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "exchangeName", value = "交易所", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "currency", value = "币种", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "klineType", value = "k线类型", required = true),
    })
    public RESTful testGetKline(String exchangeName, String currency, String klineType) {

        return currencyService.testGetKline(exchangeName, currency, klineType);
    }
}
