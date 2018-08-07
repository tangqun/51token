package com.sharex.token.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharex.token.api.entity.*;
import com.sharex.token.api.entity.enums.CodeEnum;
import com.sharex.token.api.entity.req.CurrencyCostEdit;
import com.sharex.token.api.entity.req.CurrencyPlaceOrder;
import com.sharex.token.api.entity.req.CurrencySynOrders;
import com.sharex.token.api.entity.req.ExchangeCurrencyCostEdit;
import com.sharex.token.api.entity.resp.CurrencyResp;
import com.sharex.token.api.exception.ParameterErrorException;
import com.sharex.token.api.mapper.*;
import com.sharex.token.api.util.SymbolUtil;
import com.sharex.token.api.util.ValidateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;

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
    private OrderMsgMapper orderMsgMapper;

    @Autowired
    private UserCurrencyCostMapper userCurrencyCostMapper;

    @Autowired
    private RemoteSynService remoteSynService;

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 单币聚合 - 币种详情
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
                currencyMap.put("userId", user.getId());
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

    public RESTful synOpenOrders(String token, CurrencySynOrders currencySynOrders) {
        try {

            // token valid?
            User user = userMapper.selectByToken(token);
            if (user == null) {
                return RESTful.Fail(CodeEnum.TokenInvalid);
            }
            // user status? 正常/冻结 0：正常 1：冻结
            if (0 != user.getStatus()) {
                return RESTful.Fail(CodeEnum.AccountHasBeenFrozen);
            }
            // exchange in db?
            Exchange exchange = exchangeMapper.selectEnabledByShortName(currencySynOrders.getExchangeName());
            if (exchange == null) {
                return RESTful.Fail(CodeEnum.ExchangeInvalid);
            }

            // user_exchange in db && user_exchange status?
            Map<String, Object> userApiMap = new HashMap<>();
            userApiMap.put("userId", user.getId());
            userApiMap.put("exchangeName", currencySynOrders.getExchangeName());
            UserApi userApi = userApiMapper.selectByType(userApiMap);
            if (userApi != null && 0 == userApi.getStatus()) {

                // user_currency
                Map<String, Object> currencyMap = new HashMap<>();
                currencyMap.put("exchangeName", currencySynOrders.getExchangeName());
                currencyMap.put("currency", currencySynOrders.getCurrency());
                currencyMap.put("userId", user.getId());
                UserCurrency userCurrency = userCurrencyMapper.selectEntity(currencyMap);
                if (userCurrency == null) {
                    // 尚未资产映射 或 直接调用该接口
                    return RESTful.Fail(CodeEnum.AssetInExchangeNotExistThisCurrency);
                }

                String symbol = SymbolUtil.getSymbol(userCurrency.getExchangeName(), userCurrency.getCurrency());

                remoteSynService.synOpenOrders(currencySynOrders.getExchangeName(), user.getId(),
                        userApi.getApiKey(), userApi.getApiSecret(), userCurrency.getAccountId(), symbol);

                return RESTful.Success();
            }

            // 授权不存在 or 取消了授权
            return RESTful.Fail(CodeEnum.NotExistAuthOfExchange);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return RESTful.SystemException();
        }
    }

    public RESTful synHistoryOrders(String token, CurrencySynOrders currencySynOrders) {
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
            Exchange exchange = exchangeMapper.selectEnabledByShortName(currencySynOrders.getExchangeName());
            if (exchange == null) {
                return RESTful.Fail(CodeEnum.ExchangeInvalid);
            }

            // 通过 token 获取 userId 从而获取对用的 apiKey， apiSecret 进而获取用户信息
            Map<String, Object> userApiMap = new HashMap<>();
            userApiMap.put("userId", user.getId());
            userApiMap.put("exchangeName", currencySynOrders.getExchangeName());
            UserApi userApi = userApiMapper.selectByType(userApiMap);
            if (userApi != null && userApi.getStatus().equals(0)) {

                Map<String, Object> currencyMap = new HashMap<>();
                currencyMap.put("exchangeName", currencySynOrders.getExchangeName());
                currencyMap.put("currency", currencySynOrders.getCurrency());
                currencyMap.put("userId", user.getId());
                UserCurrency userCurrency = userCurrencyMapper.selectEntity(currencyMap);
                if (userCurrency == null) {
                    // 尚未资产映射 或 直接调用该接口
                    return RESTful.Fail(CodeEnum.AssetNotSyn);
                }

                String symbol = "";
                switch (userCurrency.getExchangeName()) {
                    case "huobi": symbol = userCurrency.getCurrency() + "usdt"; break;
                    case "okex": symbol = userCurrency.getCurrency() + "_usdt"; break;
                }

                remoteSynService.synHistoryOrders(currencySynOrders.getExchangeName(), user.getId(),
                        userApi.getApiKey(), userApi.getApiSecret(), userCurrency.getAccountId(), symbol);
            }

            return RESTful.Success();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return RESTful.SystemException();
        }
    }

    public RESTful editExchangeCurrencyCost(String token, ExchangeCurrencyCostEdit exchangeCurrencyCostEdit) {
        try {

            List<CurrencyCostEdit> currencyCostEditList = exchangeCurrencyCostEdit.getCurrencyCostEditList();
            if (currencyCostEditList == null) {
                throw new ParameterErrorException("currencyCostEditList cannot be null");
            }

            for (CurrencyCostEdit currencyCostEdit:currencyCostEditList) {
                // currency 币种类型无法验证
                if (!"unit".equals(currencyCostEdit.getType()) && !"total".equals(currencyCostEdit.getType())) {
                    throw new ParameterErrorException("type must be unit/total one");
                }

                if (currencyCostEdit.getCost() <= 0d) {
                    throw new ParameterErrorException("cost must gt 0");
                }
            }

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
            Exchange exchange = exchangeMapper.selectEnabledByShortName(exchangeCurrencyCostEdit.getExchangeName());
            if (exchange == null) {
                return RESTful.Fail(CodeEnum.ExchangeInvalid);
            }

            Map<String, Object> map = new HashMap<>();
            map.put("userId", user.getId());
            map.put("exchangeName", exchangeCurrencyCostEdit.getExchangeName());
            UserApi userApi = userApiMapper.selectByType(map);
            if (userApi != null && userApi.getStatus().equals(0)) {

                // 授权并且未取消
                Map<String, Object> currencyMap = new HashMap<>();
                currencyMap.put("exchangeName", exchangeCurrencyCostEdit.getExchangeName());
                currencyMap.put("userId", user.getId());
                List<UserCurrency> userCurrencyList = userCurrencyMapper.selectList(currencyMap);

                if (userCurrencyList == null) {
                    // 已授权，不存在资产
                    return RESTful.Fail(CodeEnum.AssetInExchangeNotExistAnyCurrency);
                }

//                List<String> currencyList = userCurrencyList.stream().map(u -> u.getCurrency()).collect(Collectors.toList());

                Date date = new Date();
                List<UserCurrencyCost> userCurrencyCostList = new ArrayList<>();

                // 已授权
                for (CurrencyCostEdit currencyCostEdit:currencyCostEditList) {

                    for (UserCurrency userCurrency:userCurrencyList) {

                        if (userCurrency.getCurrency().equals(currencyCostEdit.getCurrency())) {
                            UserCurrencyCost userCurrencyCost = new UserCurrencyCost();
                            userCurrencyCost.setExchangeName(exchangeCurrencyCostEdit.getExchangeName());
                            userCurrencyCost.setCurrency(currencyCostEdit.getCurrency());
                            userCurrencyCost.setAmount(userCurrency.getFree());
                            userCurrencyCost.setCost(currencyCostEdit.getCost().toString());
                            userCurrencyCost.setType(currencyCostEdit.getType());
                            userCurrencyCost.setCreateTime(date);
                            userCurrencyCostList.add(userCurrencyCost);
                        }
                    }
                }

                if (userCurrencyCostList.size() <= 0) {
                    throw new ParameterErrorException("asset in exchange not exist the currency that you want insert into");
                }

                for (UserCurrencyCost userCurrencyCost:userCurrencyCostList) {
                    userCurrencyCostMapper.insert(userCurrencyCost);
                }

                return RESTful.Success();
            }

            // 交易所未授权
            return RESTful.Fail(CodeEnum.NotExistAuthOfExchange);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return RESTful.SystemException();
        }
    }

    /**
     *
     * @param token
     * @param currencyPlaceOrder
     * @param type buy/sell
     * @return
     */
    @Transactional
    public RESTful placeOrder(String token, CurrencyPlaceOrder currencyPlaceOrder, String type) {
        try {

            // 验证token
            if (StringUtils.isBlank(token)) {
                return RESTful.Fail(CodeEnum.TokenCannotBeNull);
            }
            if (!ValidateUtil.checkToken(token)) {
                return RESTful.Fail(CodeEnum.TokenFormatError);
            }

            if (StringUtils.isBlank(currencyPlaceOrder.getMsgId())) {
                return RESTful.Fail(CodeEnum.MsgIdCannotBeNull);
            }
            if (!ValidateUtil.checkMsgId(currencyPlaceOrder.getMsgId())) {
                return RESTful.Fail(CodeEnum.MsgIdFormatError);
            }

            User user = userMapper.selectByToken(token);
            if (user == null) {
                return RESTful.Fail(CodeEnum.TokenInvalid);
            }
            if (!user.getStatus().equals(0)) {
                return RESTful.Fail(CodeEnum.AccountHasBeenFrozen);
            }

            // exchangeName in db?
            Exchange exchange = exchangeMapper.selectEnabledByShortName(currencyPlaceOrder.getExchangeName());
            if (exchange == null) {
                return RESTful.Fail(CodeEnum.ExchangeInvalid);
            }

            Map<String, Object> userApiMap = new HashMap<>();
            userApiMap.put("userId", user.getId());
            userApiMap.put("exchangeName", currencyPlaceOrder.getExchangeName());
            UserApi userApi = userApiMapper.selectByType(userApiMap);
            if (userApi != null && userApi.getStatus().equals(0)) {

                Map<String, Object> currencyMap = new HashMap<>();
                currencyMap.put("exchangeName", currencyPlaceOrder.getExchangeName());
                currencyMap.put("currency", currencyPlaceOrder.getCurrency());
                currencyMap.put("userId", user.getId());
                UserCurrency userCurrency = userCurrencyMapper.selectEntity(currencyMap);
                if (userCurrency == null) {
                    // 尚未资产映射 或 直接调用该接口
                    return RESTful.Fail(CodeEnum.AssetNotSyn);
                }

                // 判断币种余额是否足够

                Date date = new Date();

                Integer msgIdCount = orderMsgMapper.selectCount(currencyPlaceOrder.getMsgId());
                if (msgIdCount > 0) {
                    return RESTful.Fail(CodeEnum.RepeatSubmitOrder);
                }

                // insert msg_id
                OrderMsg orderMsg = new OrderMsg();
                orderMsg.setMsgId(currencyPlaceOrder.getMsgId());
                orderMsg.setCreateTime(date);
                orderMsgMapper.insert(orderMsg);

                // 提交交易所（创建委托交易 -- 限价交易），返回 订单编号
                String symbol = null;

                switch (currencyPlaceOrder.getExchangeName()) {
                    case "huobi":
                        symbol = currencyPlaceOrder.getCurrency() + "usdt";
                        switch (type) {
                            case "buy": type = "buy-limit"; break;
                            case "sell": type = "sell-limit"; break;
                        }
                        break;
                    case "okex":
                        symbol = currencyPlaceOrder.getCurrency() + "_usdt";
                        break;
                }

                RemotePost<String> remotePost = remoteSynService.placeOrder(currencyPlaceOrder.getExchangeName(), user.getId(),
                        userApi.getApiKey(), userApi.getApiSecret(), userCurrency.getAccountId(),
                        symbol, currencyPlaceOrder.getPrice().toString(), currencyPlaceOrder.getAmount().toString(), type);

                if ("ok".equals(remotePost.getStatus())) {

                    // 订单记录数据库
                }

                // insert order_id（订单虽然记录了数据库，但是不会作为任何凭证，相当于日志），删除msg_id
                orderMsgMapper.delete(currencyPlaceOrder.getMsgId());

                return RESTful.Success();
            } else {
                // 未授权或者取消授权
                return RESTful.Fail(CodeEnum.NotExistAuthOfExchange);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
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
