package com.sharex.token.api.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharex.token.api.currency.IApiClient;
import com.sharex.token.api.currency.huobi.HuoBiApiClient;
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
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
    private HashOperations<String, String, Object> hashOperations;

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 币种聚合 - 币种详情
     * @param token
     * @param exchangeName 交易所 shortName
     * @param currency  currency（币种） 例：btc 与 symbol（符号）例：btcusdt 区别是 有没有法币转换
     * @return
     */
    public RESTful get(String token, String exchangeName, String currency) {
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
            if (user.getStatus() != 0) {
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
            if (userApi != null && userApi.getStatus() == 0) {

                Map<String, Object> currencyMap = new HashMap<>();
                currencyMap.put("exchangeName", exchangeName);
                currencyMap.put("currency", currency);
                currencyMap.put("apiKey", userApi.getApiKey());
                UserCurrency userCurrency = userCurrencyMapper.selectEntity(currencyMap);
                if (userCurrency == null) {
                    // 尚未资产映射 或 直接调用该接口
                    return RESTful.Fail(CodeEnum.AssetNotSyn);
                }

                Map<String, Object> map = new HashMap<>();

                CurrencyResp currencyResp = new CurrencyResp();
                currencyResp.setBalance(userCurrency.getBalance());

                // huobi_symbol
                String ticker = hashOperations.get("ticker", userCurrency.getExchangeName() + "_" + userCurrency.getCurrency() + "usdt_lastest").toString();
                MyKline myKline = objectMapper.readValue(ticker, MyKline.class);

                String price = myKline.getClose();

                Double vol = Double.valueOf(userCurrency.getBalance()) * Double.valueOf(myKline.getClose());
                currencyResp.setVol(vol.toString());

                // 一级数据
                map.put("currency", currencyResp);

                String kline = hashOperations.get("kline", exchangeName + "_" + currency + "usdt_1min").toString();
                List<MyKline> myKlineList = objectMapper.readValue(kline, new TypeReference<List<MyKline>>() { });

                map.put("kline", myKlineList);

                Map<String, Object> tradesMap = new HashMap<>();

                String trades_buy = hashOperations.get("trades", exchangeName + "_" + currency + "usdt_buy").toString();
                List<MyTrade> myTradeList_buy = objectMapper.readValue(trades_buy, new TypeReference<List<MyTrade>>() { });
                tradesMap.put("buy", myTradeList_buy);

                String trades_sell = hashOperations.get("trades", exchangeName + "_" + currency + "usdt_sell").toString();
                List<MyTrade> myTradeList_sell = objectMapper.readValue(trades_sell, new TypeReference<List<MyTrade>>() { });
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

            // huobi_symbol_lastest
            String respBody = hashOperations.get("ticker", exchangeName + "_" + symbol + "_lastest").toString();

            MyKline myKline = objectMapper.readValue(respBody, MyKline.class);

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

            // huobi_symbol_type 例：huobi_btcusdt_1min
            String respBody = hashOperations.get("kline", exchangeName + "_" + symbol + "_" + type).toString();

            List<MyKline> myKlineList = objectMapper.readValue(respBody, new TypeReference<List<MyKline>>() { });

            return RESTful.Success(myKlineList);

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

            // huobi_symbol_direction 例：huobi_btcusdt_buy、huobi_btcusdt_sell
            String respBody = hashOperations.get("trades", exchangeName + "_" + symbol + "_" + direction).toString();

            List<MyTrade> myTradeList = objectMapper.readValue(respBody, new TypeReference<List<MyTrade>>() { });

            return RESTful.Success(myTradeList);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return RESTful.SystemException();
        }
    }
}

class ApiFactory {

    public static IApiClient getApiClient(String exchangeName, String apiKey, String apiSecret) {
        switch (exchangeName) {
            case "huobi": return new HuoBiApiClient(apiKey, apiSecret);

//            case "okex": return new

            default: return null;
        }
    }
}
