package com.sharex.token.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharex.token.api.entity.*;
import com.sharex.token.api.entity.enums.CodeEnum;
import com.sharex.token.api.entity.resp.CurrencyResp;
import com.sharex.token.api.mapper.ExchangeMapper;
import com.sharex.token.api.mapper.UserApiMapper;
import com.sharex.token.api.mapper.UserCurrencyMapper;
import com.sharex.token.api.mapper.UserMapper;
import com.sharex.token.api.util.ValidateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class CurrencyService {

    private static final Log logger = LogFactory.getLog(CurrencyService.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserApiMapper userApiMapper;

    @Autowired
    private ExchangeMapper exchangeMapper;

    @Autowired
    private UserCurrencyMapper userCurrencyMapper;

    @Autowired
    private RemoteSynService remoteSynService;

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 币种聚合 - 币种详情
     * @param token
     * @param exchangeName 交易所 shortName
     * @param currency  currency（币种） 例：btc 与 symbol（符号）例：btcusdt 区别是 有没有法币转换
     * @return
     */
    public RESTful get(String token, String exchangeName, String currency, String klineType) {
        try {

            // 验证token
            if (StringUtils.isBlank(token)) {
                return RESTful.Fail(CodeEnum.TokenCannotBeNull);
            }
            if (!ValidateUtil.checkToken(token)) {
                return RESTful.Fail(CodeEnum.TokenFormatError);
            }

            User user = userMapper.selectByToken(token);
            if (user == null) {
                return RESTful.Fail(CodeEnum.TokenInvalid);
            }
            if (!user.getStatus().equals(0)) {
                return RESTful.Fail(CodeEnum.AccountHasBeenFrozen);
            }

            // exchangeName in db?
            Exchange exchange = exchangeMapper.selectEnabledByShortName(exchangeName);
            if (exchange == null) {
                return RESTful.Fail(CodeEnum.ExchangeInvalid);
            }

            // 通过 token 获取 userId 从而获取对用的 apiKey， apiSecret 进而获取用户信息
            Map<String, Object> userApiMap = new HashMap<>();
            userApiMap.put("userId", user.getId());
            userApiMap.put("exchangeName", exchangeName);
            UserApi userApi = userApiMapper.selectByType(userApiMap);
            if (userApi != null && userApi.getStatus().equals(0)) {

                Map<String, Object> currencyMap = new HashMap<>();
                currencyMap.put("exchangeName", exchangeName);
                currencyMap.put("currency", currency);
                currencyMap.put("apiKey", userApi.getApiKey());
                UserCurrency userCurrency = userCurrencyMapper.selectEntity(currencyMap);
                if (userCurrency == null) {
                    // 尚未资产映射 或 直接调用该接口
                    return RESTful.Fail(CodeEnum.AssetNotSyn);
                }

                /**
                 * {
                 *   code:
                 *   msg:
                 *   data: {
                 *       currency: { },
                 *       kline: [],
                 *       trades: {
                 *           buy: { },
                 *           sell: { }
                 *       }
                 *   }
                 * }
                 */
                Map<String, Object> map = new HashMap<>();

                // 单币资产
                CurrencyResp currencyResp = new CurrencyResp();

                String symbol = null;

                switch (userCurrency.getExchangeName()) {
                    case "huobi": symbol = userCurrency.getCurrency() + "usdt"; break;
                    case "okex": symbol = userCurrency.getCurrency() + "_usdt"; break;
                }
                List<MyKline> myKlineList = remoteSynService.getKline(userCurrency.getExchangeName(), symbol, "1min");
                MyKline myKline = myKlineList.get(0);
                currencyResp.setPrice(myKline.getClose());
                Double vol = Double.valueOf(userCurrency.getFree()) * Double.valueOf(myKline.getClose());
                currencyResp.setVol(vol.toString());
                // data: { currency: }
                map.put("currency", currencyResp);


                if (!"1min".equals(klineType)) {
                    myKlineList = remoteSynService.getKline(userCurrency.getExchangeName(), symbol, klineType);
                }
                map.put("kline", myKlineList);

                Map<String, Object> tradesMap = new HashMap<>();
                MyTrades myTrades = remoteSynService.getTrades(exchangeName, symbol); //hashOperations.get("trades", exchangeName + "_" + currency + "usdt_buy").toString();
                List<MyTrade> myTradeList_buy = myTrades.getBuy().subList(0, 10);
                tradesMap.put("buy", myTradeList_buy);
                List<MyTrade> myTradeList_sell = myTrades.getSell().subList(0, 10);
                tradesMap.put("sell", myTradeList_sell);
                map.put("trades", tradesMap);

                return RESTful.Success(map);

            } else {
                // 未授权或者取消授权
                return RESTful.Fail(CodeEnum.NotExistAuthOfExchange);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return RESTful.SystemException();
        }
    }

    /**
     * 获取行情
     * @param exchangeName 交易所 shortName
     * @param symbol 币种 火币 btcusdt okex btc_usdt
     * @return
     */
    public RESTful getTicker(String exchangeName, String symbol) {
        try {
            List<MyKline> myKlineList = remoteSynService.getKline(exchangeName, symbol, "1min");
            MyKline myKline = myKlineList.get(0);
            return RESTful.Success(myKline);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return RESTful.SystemException();
        }
    }

    /**
     * k线接口
     * @param exchangeName
     * @param symbol
     * @param type
     * @return
     */
    public RESTful getKline(String exchangeName, String symbol, String type) {
        try {
            List<MyKline> myKlineList = remoteSynService.getKline(exchangeName, symbol, type);
            return RESTful.Success(myKlineList);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return RESTful.SystemException();
        }
    }

    public RESTful getTrades(String exchangeName, String symbol) {
        try {
            Map<String, Object> tradesMap = new HashMap<>();
            MyTrades myTrades = remoteSynService.getTrades(exchangeName, symbol);
            List<MyTrade> myTradeList_buy = myTrades.getBuy().subList(0, 10);
            tradesMap.put("buy", myTradeList_buy);
            List<MyTrade> myTradeList_sell = myTrades.getSell().subList(0, 10);
            tradesMap.put("sell", myTradeList_sell);
            return RESTful.Success(tradesMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return RESTful.SystemException();
        }
    }

    /**
     * 最新成交
     * @param exchangeName
     * @param symbol
     * @param direction buy/sell
     * @return
     */
    public RESTful getTrades(String exchangeName, String symbol, String direction) {
        try {
            MyTrades myTrades = remoteSynService.getTrades(exchangeName, symbol); //hashOperations.get("trades", exchangeName + "_" + currency + "usdt_buy").toString();

//            Map<String, Object> tradesMap = new HashMap<>();
//            List<MyTrade> myTradeList_buy = myTrades.getBuy().subList(0, 10);
//            tradesMap.put("buy", myTradeList_buy);
//            List<MyTrade> myTradeList_sell = myTrades.getSell().subList(0, 10);
//            tradesMap.put("sell", myTradeList_sell);

            List<MyTrade> myTradeList = new LinkedList<>();
            if ("buy".equals(direction)) {
                myTradeList = myTrades.getBuy().subList(0, 10);
            } else if ("sell".equals(direction)) {
                myTradeList = myTrades.getSell().subList(0, 10);
            }
            return RESTful.Success(myTradeList);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return RESTful.SystemException();
        }
    }
}
