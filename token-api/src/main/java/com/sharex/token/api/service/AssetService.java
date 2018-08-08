package com.sharex.token.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharex.token.api.entity.*;
import com.sharex.token.api.entity.enums.CodeEnum;
import com.sharex.token.api.entity.req.AssetAuth;
import com.sharex.token.api.entity.req.AssetRmAuth;
import com.sharex.token.api.entity.req.AssetSyn;
import com.sharex.token.api.entity.resp.AssetResp;
import com.sharex.token.api.entity.resp.ExchangeResp;
import com.sharex.token.api.entity.resp.UserCurrencyAssetResp;
import com.sharex.token.api.entity.resp.UserExchangeAssetResp;
import com.sharex.token.api.mapper.*;
import com.sharex.token.api.util.SymbolUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AssetService {

    private static final Log logger = LogFactory.getLog(AssetService.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserApiMapper userApiMapper;

    @Autowired
    private ExchangeMapper exchangeMapper;

    @Autowired
    private UserCurrencyMapper userCurrencyMapper;

    @Autowired
    private UserCurrencyCostMapper userCurrencyCostMapper;

    @Autowired
    private RemoteSynService remoteSynService;

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 获取授权映射列表
     * @param token
     * @return
     */
    public RESTful getAuthMapping(String token) {
        try {

            User user = userMapper.selectByToken(token);
            if (user == null) {
                return RESTful.Fail(CodeEnum.TokenInvalid);
            }
            if (!user.getStatus().equals(0)) {
                return RESTful.Fail(CodeEnum.AccountHasBeenFrozen);
            }

            List<Exchange> exchangeList = exchangeMapper.selectEnabled();
            if (exchangeList == null) {
                return RESTful.Fail(CodeEnum.ExchangeInDBNotConfig);
            }

            /**
             * {
             *   code:
             *   msg:
             *   data: {
             *     exchange: [{}, {}, {}]
             *   }
             * }
             */

            // data
            Map<String, Object> map = new HashMap<>();

            // exchange
            List<ExchangeResp> exchangeRespList = new ArrayList<>();

            List<UserApi> userApiList = userApiMapper.selectEnabledByUserId(user.getId());
            for (Exchange exchange:exchangeList) {

                ExchangeResp exchangeResp = new ExchangeResp();
                exchangeResp.setLogo(exchange.getLogo());
                exchangeResp.setName(exchange.getName());
                exchangeResp.setShortName(exchange.getShortName());
                exchangeResp.setAuthTutorialUrl(exchange.getAuthTutorialUrl());
                exchangeResp.setOfficialUrl(exchange.getOfficialUrl());

                if (userApiList != null) {
                        // 集合数据合并，生成新数据输出
                    if (userApiList.stream().anyMatch(up -> up.getExchangeName().equals(exchange.getShortName()))) {

                        exchangeResp.setAuthStatus(0);
                    } else {

                        exchangeResp.setAuthStatus(1);
                    }
                }

                exchangeRespList.add(exchangeResp);
            }

            map.put("exchange", exchangeRespList);

            return RESTful.Success(map);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return RESTful.SystemException();
        }
    }

    /**
     * 授权
     * @param token
     * @param assetAuth
     * @return
     */
    public RESTful auth(String token, AssetAuth assetAuth) {
        try {

            User user = userMapper.selectByToken(token);
            if (user == null) {
                return RESTful.Fail(CodeEnum.TokenInvalid);
            }
            if (!user.getStatus().equals(0)) {
                return RESTful.Fail(CodeEnum.AccountHasBeenFrozen);
            }

            // exchangeName in db?
            Exchange exchange = exchangeMapper.selectEnabledByShortName(assetAuth.getExchangeName());
            if (exchange == null) {
                return RESTful.Fail(CodeEnum.ExchangeInvalid);
            }

            // 当前时间
            Date date = new Date();

            // 授权时候尝试获取相应的用户信息接口再保存，无效的数据没有意义
            // 同一个人同平台换 ApiKey ApiSecret 重复授权问题
            Map<String, Object> typeMap = new HashMap<>();
            typeMap.put("userId", user.getId());
            typeMap.put("exchangeName", assetAuth.getExchangeName());
            UserApi userApi = userApiMapper.selectByType(typeMap);
            if (userApi != null) {
                // 已授权过，判断状态
                if (userApi.getStatus().equals(0)) {
                    // 请勿重复授权
                    return RESTful.Fail(CodeEnum.RepeatAuthOfAsset);
                } else {
                    // 更新授权

                    Map<String, Object> updateMap = new HashMap<>();
                    updateMap.put("apiKey", assetAuth.getApiKey());
                    updateMap.put("apiSecret", assetAuth.getApiSecret());
                    updateMap.put("userId", user.getId());
                    updateMap.put("exchangeName", assetAuth.getExchangeName());
                    // 设置有效
                    updateMap.put("status", 0);
                    updateMap.put("updateTime", date);
                    userApiMapper.update(updateMap);
                }
            } else {
                // 未授权过
                userApi = new UserApi();
                userApi.setApiKey(assetAuth.getApiKey());
                userApi.setApiSecret(assetAuth.getApiSecret());
                userApi.setExchangeName(assetAuth.getExchangeName());
                userApi.setUserId(user.getId());
                userApi.setCreateTime(date);
                userApiMapper.insert(userApi);
            }

            return RESTful.Success();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return RESTful.SystemException();
        }
    }

    /**
     * 取消授权
     * @param token
     * @param assetRmAuth
     * @return
     */
    public RESTful rmAuth(String token, AssetRmAuth assetRmAuth) {
        try {

            User user = userMapper.selectByToken(token);
            if (user == null) {
                return RESTful.Fail(CodeEnum.TokenInvalid);
            }
            if (!user.getStatus().equals(0)) {
                return RESTful.Fail(CodeEnum.AccountHasBeenFrozen);
            }

            // exchangeName in db?
            Exchange exchange = exchangeMapper.selectEnabledByShortName(assetRmAuth.getExchangeName());
            if (exchange == null) {
                return RESTful.Fail(CodeEnum.ExchangeInvalid);
            }

            Map<String, Object> typeMapper = new HashMap<>();
            typeMapper.put("userId", user.getId());
            typeMapper.put("exchangeName", assetRmAuth.getExchangeName());
            UserApi userApi = userApiMapper.selectByType(typeMapper);
            if (userApi == null) {
                // 请勿重复取消授权
                return RESTful.Fail(CodeEnum.RepeatRmAuthOfAsset);
            }
            if (!userApi.getStatus().equals(0)) {
                return RESTful.Fail(CodeEnum.RepeatRmAuthOfAsset);
            }

            Date date = new Date();

            Map<String, Object> statusMap= new HashMap<>();
            statusMap.put("userId", user.getId());
            statusMap.put("exchangeName", assetRmAuth.getExchangeName());
            // 设置失效
            statusMap.put("status", 1);
            statusMap.put("updateTime", date);
            userApiMapper.updateStatus(statusMap);

            return RESTful.Success();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return RESTful.SystemException();
        }
    }

    /**
     * 资产聚合
     * @param token
     * @return
     */
    // 获取币种最新市值
    // huobi
    //   ticker_btcusdt 暂时不用，读取 kline_symbol_1min 第一条数据
    //   kline_btcusdt_1min
    //   kline_btcusdt_15min
    //   ...
    //   trade_btcusdt_buy
    //   trade_btcusdt_sell
    public RESTful getAsset(String token) {
        try {

            Double rate = 6.18d;
            String rateStr = "￥";

            User user = userMapper.selectByToken(token);
            if (user == null) {
                return RESTful.Fail(CodeEnum.TokenInvalid);
            }
            if (!user.getStatus().equals(0)) {
                return RESTful.Fail(CodeEnum.AccountHasBeenFrozen);
            }

            // 返回
            Map<String, Object> map = new HashMap<>();

            // {
            //   code:
            //   msg:
            //   data: {
            //       asset: [
            //           { name: huobi, currencyCount: , profit: , cumulativeProfit: , userCurrencyAssetRespList: [
            //              { exchangeName: , free: , freezed: , ... },
            //              { exchangeName: , free: , freezed: , ... },
            //              { exchangeName: , free: , freezed: , ... }]},
            //           { name: okex, currencyCount: , profit: , cumulativeProfit: , userCurrencyAssetRespList: [
            //              { exchangeName: , free: , freezed: , ... },
            //              { exchangeName: , free: , freezed: , ... },
            //              { exchangeName: , free: , freezed: , ... }]},
            //           { name: binance, currencyCount: , profit: , cumulativeProfit: , userCurrencyAssetRespList: [
            //              { exchangeName: , free: , freezed: , ... },
            //              { exchangeName: , free: , freezed: , ... },
            //              { exchangeName: , free: , freezed: , ... }]}
            //       ]
            //    }
            // }
            AssetResp assetResp = new AssetResp();

            Double vol = null;
            Double profit = null;
            Double cumulativeProfit = null;
            Double cost = null;


            // 交易所数据集合
            List<UserExchangeAssetResp> userExchangeAssetRespList = new ArrayList<>();

            // 账号下授权的交易所集合
            List<UserApi> userApiList = userApiMapper.selectEnabledByUserId(user.getId());
            if (userApiList.size() > 0) {
                vol = 0d;
                profit = 0d;
                cumulativeProfit = 0d;
                cost = 0d;

                for (UserApi userApi : userApiList) {

                    // 单交易所数据
                    UserExchangeAssetResp userExchangeAssetResp = new UserExchangeAssetResp();

                    // 设置币种名称
                    userExchangeAssetResp.setName(userApi.getExchangeName());

                    Double exchangeVol = null;
                    Double exchangeProfit = null;
                    Double exchangeCumulativeProfit = null;
                    Double exchangeCost = null;

                    // 单交易所下的单币数据集合
                    List<UserCurrencyAssetResp> userCurrencyAssetRespList = new ArrayList<>();

                    Map<String, Object> userCurrencyMap = new HashMap<>();
                    userCurrencyMap.put("exchangeName", userApi.getExchangeName());
                    userCurrencyMap.put("userId", user.getId());
                    List<UserCurrency> userCurrencyList = userCurrencyMapper.selectList(userCurrencyMap);
                    if (userCurrencyList.size() > 0) {
                        //
                        exchangeVol = 0d;
                        exchangeProfit = 0d;
                        exchangeCumulativeProfit = 0d;
                        exchangeCost = 0d;

                        for (UserCurrency userCurrency : userCurrencyList) {

                            // 单币数据
                            UserCurrencyAssetResp userCurrencyAssetResp = new UserCurrencyAssetResp();
                            // 交易所
                            userCurrencyAssetResp.setExchangeName(userCurrency.getExchangeName());
                            // 币种
                            userCurrencyAssetResp.setCurrency(userCurrency.getCurrency());
                            // 数量
                            userCurrencyAssetResp.setFree(userCurrency.getFree());
                            // 冻结数量
                            userCurrencyAssetResp.setFreezed(userCurrency.getFreezed());

                            String symbol = SymbolUtil.getSymbol(userCurrency.getExchangeName(), userCurrency.getCurrency());
                            if ("usdt".equals(userCurrency.getCurrency())) {

                                // 市值
                                userCurrencyAssetResp.setVol(userCurrency.getFree());
                                // 现价
                                userCurrencyAssetResp.setPrice(userCurrency.getFree());
                                // 成本
                                userCurrencyAssetResp.setCost(userCurrency.getFree());

                                // 交易所累计市值
                                exchangeVol += Double.valueOf(userCurrency.getFree());

                                // 交易所累计成本
                                exchangeCost += Double.valueOf(userCurrency.getFree());

                            } else {
                                // kline第一条作为行情
                                List<MyKline> myKlineList = remoteSynService.getKline(userCurrency.getExchangeName(), symbol, "1min");
                                MyKline myKline = myKlineList.get(0);

                                // 市值
                                Double currencyVol = Double.valueOf(userCurrency.getFree()) * Double.valueOf(myKline.getClose());
                                userCurrencyAssetResp.setVol(currencyVol.toString());

                                // 现价
                                userCurrencyAssetResp.setPrice(myKline.getClose());

                                // 成本价
                                userCurrencyAssetResp.setCost(userCurrency.getCost());

                                // 当日收益 (close - open) * free
                                Double currencyProfit = (Double.valueOf(myKline.getClose()) - Double.valueOf(myKline.getOpen())) * Double.valueOf(userCurrency.getFree());
                                userCurrencyAssetResp.setProfit(currencyProfit.toString());

                                // 收益率 (当日收益 / 最新价) * 100
                                Double currencyProfitRate = (currencyProfit / Double.valueOf(myKline.getClose())) * 100;

                                userCurrencyAssetResp.setProfitRate(currencyProfitRate.toString());


                                // 本地成本，如果存在配置成本，则修改
//                            Map<String, Object> userCurrencyCostMap = new HashMap<>();
//                            userCurrencyCostMap.put("exchangeName", userCurrency.getExchangeName());
//                            userCurrencyCostMap.put("currency", userCurrency.getCurrency());
//                            userCurrencyCostMap.put("userId", user.getId());
//                            UserCurrencyCost userCurrencyCost = userCurrencyCostMapper.selectEntity(userCurrencyCostMap);
//                            if (userCurrencyCost != null) {
//
//                            }


                                // 交易所累计市值
                                exchangeVol += currencyVol;

                                Double currencyCost = Double.valueOf(userCurrency.getFree()) * Double.valueOf(userCurrency.getCost());
                                // 交易所累计成本
                                exchangeCost += currencyCost;

                                // 交易所累计当日收益
                                exchangeProfit += currencyProfit;

                                // 累计收益 (close - cost) * free
                                Double currencyCumulativeProfit = (Double.valueOf(myKline.getClose()) - Double.valueOf(userCurrency.getCost())) * Double.valueOf(userCurrency.getFree());
                                exchangeCumulativeProfit += currencyCumulativeProfit;

                            }

                            userCurrencyAssetRespList.add(userCurrencyAssetResp);
                        }

                        //
                        vol += exchangeVol;
                        profit += exchangeProfit;
                        cumulativeProfit += exchangeCumulativeProfit;
                        cost += exchangeCost;
                    }

                    // 设置单交易所市值
                    if (null != exchangeVol) {
                        userExchangeAssetResp.setVol(exchangeVol.toString());
                    }

                    // 设置单交易所今日收益
                    if (null != exchangeProfit) {
                        userExchangeAssetResp.setProfit(exchangeProfit.toString());
                    }

                    // 设置单交易所累计收益
                    if (null != exchangeCumulativeProfit) {
                        userExchangeAssetResp.setCumulativeProfit(exchangeCumulativeProfit.toString());
                    }

                    // 设置但交易所成本
                    if (null != exchangeCost) {
                        userExchangeAssetResp.setCost(exchangeCost.toString());
                    }

                    // 设置单交易所数字币数据集合
                    userExchangeAssetResp.setUserCurrencyAssetRespList(userCurrencyAssetRespList);

                    userExchangeAssetRespList.add(userExchangeAssetResp);
                }
            }

            // 总资产
            if (null != vol) {
                assetResp.setVol(vol.toString());
            }

            // 今日收益
            if (null != profit) {
                assetResp.setProfit(profit.toString());
            }

            // 累计收益
            if (null != cumulativeProfit) {
                assetResp.setCumulativeProfit(cumulativeProfit.toString());
            }

            // 收益率
            if (null != vol && null != cost) {
                assetResp.setProfitRate(String.valueOf((vol - cost) / vol));
            }

            // 设置交易所数据集合
            assetResp.setUserExchangeAssetRespList(userExchangeAssetRespList);

            // 设置map
            map.put("asset", assetResp);

            return RESTful.Success(map);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return RESTful.SystemException();
        }
    }

    public RESTful getExchangeAsset(String token, String exchangeName) {
        try {

            User user = userMapper.selectByToken(token);
            if (user == null) {
                return RESTful.Fail(CodeEnum.TokenInvalid);
            }
            if (!user.getStatus().equals(0)) {
                return RESTful.Fail(CodeEnum.AccountHasBeenFrozen);
            }

            // 账号下授权的交易所集合
            Map<String, Object> userExchangeMap = new HashMap<>();
            userExchangeMap.put("exchangeName", exchangeName);
            userExchangeMap.put("userId", user.getId());
            UserApi userApi = userApiMapper.selectEnabledEntity(userExchangeMap);
            if (userApi != null && 0 == userApi.getStatus()) {
                // 单交易所数据
                UserExchangeAssetResp userExchangeAssetResp = new UserExchangeAssetResp();

                // 设置币种名称
                userExchangeAssetResp.setName(userApi.getExchangeName());

                Double exchangeProfit = 0d;
                Double exchangeVol = 0d;
                Double exchangeCumulativeProfit = 0d;
                Double exchangeCost = 0d;

                // 单交易所下的单币数据集合
                List<UserCurrencyAssetResp> userCurrencyAssetRespList = new ArrayList<>();

                Map<String, Object> userCurrencyMap = new HashMap<>();
                userCurrencyMap.put("exchangeName", userApi.getExchangeName());
                userCurrencyMap.put("userId", user.getId());
                List<UserCurrency> userCurrencyList = userCurrencyMapper.selectList(userCurrencyMap);
                for (UserCurrency userCurrency : userCurrencyList) {

                    // 单币数据
                    UserCurrencyAssetResp userCurrencyAssetResp = new UserCurrencyAssetResp();
                    userCurrencyAssetResp.setExchangeName(userCurrency.getExchangeName());
                    userCurrencyAssetResp.setCurrency(userCurrency.getCurrency());
                    userCurrencyAssetResp.setFree(userCurrency.getFree());
                    userCurrencyAssetResp.setFreezed(userCurrency.getFreezed());

                    String symbol = SymbolUtil.getSymbol(userCurrency.getExchangeName(), userCurrency.getCurrency());

                    // 获取币种最新市值
                    // huobi
                    //   ticker_btcusdt 暂时不用，读取 kline_symbol_1min 第一条数据
                    //   kline_btcusdt_1min
                    //   kline_btcusdt_15min
                    //   ...
                    //   trade_btcusdt_buy
                    //   trade_btcusdt_sell
                    if ("usdt".equals(userCurrency.getCurrency())) {

                        userCurrencyAssetResp.setPrice(userCurrency.getFree());

                        userCurrencyAssetResp.setVol(userCurrency.getFree());

                        //
                        exchangeVol += Double.valueOf(userCurrency.getFree());

                        userCurrencyAssetResp.setCost(userCurrency.getFree());

                        //
                        exchangeCost += Double.valueOf(userCurrency.getFree());

                    } else {
                        List<MyKline> myKlineList = remoteSynService.getKline(userCurrency.getExchangeName(), symbol, "1min");
                        MyKline myKline = myKlineList.get(0);
                        // 现价
                        userCurrencyAssetResp.setPrice(myKline.getClose());

                        // 市值
                        Double currencyVol = Double.valueOf(userCurrency.getFree()) * Double.valueOf(myKline.getClose());
                        userCurrencyAssetResp.setVol(currencyVol.toString());

                        exchangeVol += currencyVol;

                        // 成本
                        Double cost = Double.valueOf(userCurrency.getFree()) * Double.valueOf(userCurrency.getCost());
                        userCurrencyAssetResp.setCost(cost.toString());
                        // 累计收益 = (最新价 - 成本价) * 数量
                        Double currencyCumulativeProfit = (Double.valueOf(myKline.getClose()) - Double.valueOf(userCurrency.getCost())) * Double.valueOf(userCurrency.getFree());

                        exchangeCost += Double.valueOf(userCurrency.getFree()) * Double.valueOf(userCurrency.getCost());

                        exchangeCumulativeProfit += currencyCumulativeProfit;

                        // 当日收益
                        Double currencyProfit = (Double.valueOf(myKline.getClose()) - Double.valueOf(myKline.getOpen())) * Double.valueOf(userCurrency.getFree());
                        userCurrencyAssetResp.setProfit(currencyProfit.toString());

                        exchangeProfit += currencyProfit;

                        Double currencyProfitRate = (currencyProfit / Double.valueOf(myKline.getClose())) * 100;

                        userCurrencyAssetResp.setProfitRate(currencyProfitRate.toString());
                    }

                    userCurrencyAssetRespList.add(userCurrencyAssetResp);
                }

                // 总市值
                userExchangeAssetResp.setVol(exchangeVol.toString());

                // 总成本
                userExchangeAssetResp.setCost(exchangeCost.toString());

                // 累计收益
                userExchangeAssetResp.setCumulativeProfit(exchangeCumulativeProfit.toString());

                userExchangeAssetResp.setUserCurrencyAssetRespList(userCurrencyAssetRespList);

                return RESTful.Success(userExchangeAssetResp);
            }

            return RESTful.Fail(CodeEnum.NotExistAuthOfExchange);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return RESTful.SystemException();
        }
    }

    /**
     * 远程同步资产
     * @param token
     * @return
     */
    public RESTful syn(String token, AssetSyn assetSyn) {
        try {

            User user = userMapper.selectByToken(token);
            if (user == null) {
                return RESTful.Fail(CodeEnum.TokenInvalid);
            }
            if (!user.getStatus().equals(0)) {
                return RESTful.Fail(CodeEnum.AccountHasBeenFrozen);
            }

            // exchangeName in db?
            Exchange exchange = exchangeMapper.selectEnabledByShortName(assetSyn.getExchangeName());
            if (exchange == null) {
                return RESTful.Fail(CodeEnum.ExchangeInvalid);
            }

            Map<String, Object> typeMapper = new HashMap<>();
            typeMapper.put("userId", user.getId());
            typeMapper.put("exchangeName", assetSyn.getExchangeName());
            UserApi userApi = userApiMapper.selectByType(typeMapper);
            if (userApi == null) {
                // 授权不存在
                return RESTful.Fail(CodeEnum.NotExistAuthOfExchange);
            }
            if (!userApi.getStatus().equals(0)) {
                return RESTful.Fail(CodeEnum.NotExistAuthOfExchange);
            }

            Map<String, UserCurrency> map = null;

            switch (assetSyn.getExchangeName()) {
                case "huobi": remoteSynService.synAccounts("huobi", user.getId(), userApi.getApiKey(), userApi.getApiSecret()); break;
                default: break;
            }

            return RESTful.Success();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return RESTful.SystemException();
        }
    }

//    private RESTful synHuoBi(String exchangeName, Integer userId, String apiKey, String apiSecret) throws Exception {
//
//        Date date = new Date();
//
//        // 获取用户信息，保存数据库
//        IApiClient apiClient = new HuoBiApiClient(apiKey, apiSecret);
//        String respBody = apiClient.accounts();
//
//        if (logger.isDebugEnabled()) {
//            logger.debug(respBody);
//        }
//
//        if (!StringUtils.isBlank(respBody)) {
//            ApiResp apiResp = objectMapper.readValue(respBody, ApiResp.class);
//            if ("ok".equals(apiResp.getStatus())) {
//                Map<String, UserCurrency> map = new HashMap<>();
//
//                Account account = objectMapper.convertValue(apiResp.getData(), Account.class);
//                List<Balance> balanceList = objectMapper.convertValue(account.getList(), new TypeReference<List<Balance>>() { });
//                // 非安全
//                for (Balance balance:balanceList) {
//                    if (Double.valueOf(balance.getBalance()) > 0) {
//                        // 根据币种判断 map 是否含有对象
//                        UserCurrency userCurrency = map.get(balance.getCurrency());
//                        if (userCurrency == null) {
//                            userCurrency = new UserCurrency();
//                            userCurrency.setFree("0");
//                            userCurrency.setFreezed("0");
//                        }
//                        userCurrency.setExchangeName(exchangeName);
//                        userCurrency.setCurrency(balance.getCurrency());
//                        if ("trade".equals(balance.getType())) {
//                            userCurrency.setFree(balance.getBalance());
//                        }else {
//                            // "frozen"
//                            userCurrency.setFreezed(balance.getBalance());
//                        }
//                        userCurrency.setUserId(userId);
//                        userCurrency.setApiKey(apiKey);
//                        userCurrency.setApiSecret(apiSecret);
//                        userCurrency.setAccountId(account.getId().toString());
//                        userCurrency.setCreateTime(date);
//                        map.put(balance.getCurrency(), userCurrency);
//                    }
//                }
//                saveUserAsset(exchangeName, userId, map);
//                return RESTful.Success();
//            }
//
//            // 交易所异常
//            return RESTful.Fail(CodeEnum.ExchangeInternalError, apiResp.errMsg);
//        }
//
//        // 网络异常
//        return RESTful.Fail(CodeEnum.NetworkError);
//    }
//
//    @Transactional
//    void saveUserAsset(String exchangeName, Integer userId, Map<String, UserCurrency> map) {
//
//        Map<String, Object> deleteMap = new HashMap<>();
//        deleteMap.put("exchangeName", exchangeName);
//        deleteMap.put("userId", userId);
//        userCurrencyMapper.delete(deleteMap);
//
//        Set<Map.Entry<String, UserCurrency>> entrySet = map.entrySet();
//        for (Map.Entry<String, UserCurrency> entry:entrySet) {
//            userCurrencyMapper.insert(entry.getValue());
//        }
//    }
}
