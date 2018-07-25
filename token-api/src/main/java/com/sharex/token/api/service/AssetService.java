package com.sharex.token.api.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharex.token.api.currency.IApiClient;
import com.sharex.token.api.currency.huobi.HuoBiApiClient;
import com.sharex.token.api.currency.huobi.resp.Account;
import com.sharex.token.api.currency.huobi.resp.ApiResp;
import com.sharex.token.api.currency.huobi.resp.Balance;
import com.sharex.token.api.entity.*;
import com.sharex.token.api.entity.enums.CodeEnum;
import com.sharex.token.api.entity.req.AssetAuth;
import com.sharex.token.api.entity.req.AssetRmAuth;
import com.sharex.token.api.entity.req.AssetSyn;
import com.sharex.token.api.entity.resp.AssetResp;
import com.sharex.token.api.entity.resp.ExchangeResp;
import com.sharex.token.api.entity.resp.UserCurrencyAssetResp;
import com.sharex.token.api.entity.resp.UserExchangeAssetResp;
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
import org.springframework.transaction.annotation.Transactional;

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

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private HashOperations<String, String, Object> hashOperations;

    /**
     * 获取授权映射列表
     * @param token
     * @return
     */
    public RESTful getAuthMapping(String token) {
        try {
            // 验证token
            if (StringUtils.isEmpty(token.trim())) {
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

            List<Exchange> exchangeList = exchangeMapper.selectEnabled();
            if (exchangeList == null) {
                return RESTful.Fail(CodeEnum.ExchangeInDBNotConfig);
            }

            // 一级
            Map<String, Object> map = new HashMap<>();

            // 二级
            List<ExchangeResp> exchangeRespList = new ArrayList<>();


            List<UserApi> userApiList = userApiMapper.selectEnabledByUserId(user.getId());

            for (Exchange exchange:exchangeList) {

                ExchangeResp exchangeResp = new ExchangeResp();
                exchangeResp.setLogo(exchange.getLogo());
                exchangeResp.setName(exchange.getName());
                exchangeResp.setShortName(exchange.getShortName());

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

            // 验证token
            if (StringUtils.isEmpty(token.trim())) {
                return RESTful.Fail(CodeEnum.TokenCannotBeNull);
            }
            if (!ValidateUtil.checkToken(token)) {
                return RESTful.Fail(CodeEnum.TokenFormatError);
            }

            if (StringUtils.isEmpty(assetAuth.getApiKey().trim())) {
                return RESTful.Fail(CodeEnum.ApiKeyCannotBeNull);
            }

            if (StringUtils.isEmpty(assetAuth.getApiSecret().trim())) {
                return RESTful.Fail(CodeEnum.ApiSecretCannotBeNull);
            }

            User user = userMapper.selectByToken(token);
            if (user == null) {
                return RESTful.Fail(CodeEnum.TokenInvalid);
            }
            if (user.getStatus() != 0) {
                return RESTful.Fail(CodeEnum.AccountHasBeenFrozen);
            }

            // exchangeName in db?
            Exchange exchange = exchangeMapper.selectEnabledByShortName(assetAuth.getExchangeName());
            if (exchange == null) {
                return RESTful.Fail(CodeEnum.ExchangeInvalid);
            }

            // 授权时候尝试获取相应的用户信息接口再保存，无效的数据没有意义
            // 同一个人同平台换 ApiKey ApiSecret 重复授权问题
            Map<String, Object> typeMapper = new HashMap<>();
            typeMapper.put("userId", user.getId());
            typeMapper.put("exchangeName", assetAuth.getExchangeName());
            UserApi userApi = userApiMapper.selectEnabledByType(typeMapper);
            if (userApi != null) {
                // 请勿重复授权
                return RESTful.Fail(CodeEnum.RepeatAuthOfAsset);
            }

            Date date = new Date();

            userApi = new UserApi();
            userApi.setApiKey(assetAuth.getApiKey());
            userApi.setApiSecret(assetAuth.getApiSecret());
            userApi.setExchangeName(assetAuth.getExchangeName());
            userApi.setUserId(user.getId());
            userApi.setCreateTime(date);
            userApiMapper.insert(userApi);

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

            // 验证token
            if (StringUtils.isEmpty(token.trim())) {
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
            Exchange exchange = exchangeMapper.selectEnabledByShortName(assetRmAuth.getExchangeName());
            if (exchange == null) {
                return RESTful.Fail(CodeEnum.ExchangeInvalid);
            }

            Map<String, Object> typeMapper = new HashMap<>();
            typeMapper.put("userId", user.getId());
            typeMapper.put("exchangeName", assetRmAuth.getExchangeName());
            UserApi userApi = userApiMapper.selectEnabledByType(typeMapper);
            if (userApi == null) {
                // 请勿重复取消授权
                return RESTful.Fail(CodeEnum.RepeatRmAuthOfAsset);
            }

            Date date = new Date();

            Map<String, Object> statusMapper = new HashMap<>();
            statusMapper.put("userId", user.getId());
            statusMapper.put("exchangeName", assetRmAuth.getExchangeName());
            statusMapper.put("updateTime", date);
            userApiMapper.updateStatus(statusMapper);

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
    public RESTful getAsset(String token) {
        try {

            // 验证token
            if (StringUtils.isEmpty(token.trim())) {
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

            // 返回
            Map<String, Object> map = new HashMap<>();

            // 一级
            AssetResp assetResp = new AssetResp();

            // 二级
            List<UserExchangeAssetResp> userExchangeAssetRespList = new ArrayList<>();

            // 获取所有交易所
            List<UserApi> userApiList = userApiMapper.selectEnabledByUserId(user.getId());
            for (UserApi userApi:userApiList) {

                UserExchangeAssetResp userExchangeAssetResp = new UserExchangeAssetResp();
                // 设置币种名称
                userExchangeAssetResp.setName(userApi.getExchangeName());

                // 设置币种接口名称？

                // 三级
                List<UserCurrencyAssetResp> userCurrencyAssetRespList = new ArrayList<>();

                List<UserCurrency> userCurrencyList = userCurrencyMapper.selectByApiKey(userApi.getApiKey());
                for (UserCurrency userCurrency:userCurrencyList) {

                    if ("trade".equals(userCurrency.getType())) {
                        // 三级
                        UserCurrencyAssetResp userCurrencyAssetResp = new UserCurrencyAssetResp();
                        userCurrencyAssetResp.setExchangeName(userCurrency.getExchangeName());
                        userCurrencyAssetResp.setCurrency(userCurrency.getCurrency());
                        userCurrencyAssetResp.setType(userCurrency.getType());
                        userCurrencyAssetResp.setBalance(userCurrency.getBalance());

                        // huobi_symbol
                        String ticker = hashOperations.get("ticker", userCurrency.getExchangeName() + "_" + userCurrency.getCurrency() + "usdt_lastest").toString();
                        MyKline myKline = objectMapper.readValue(ticker, MyKline.class);
                        userCurrencyAssetResp.setPrice(myKline.getClose());

                        //
                        Double vol = Double.valueOf(userCurrency.getBalance()) * Double.valueOf(myKline.getClose());
                        userCurrencyAssetResp.setVol(vol.toString());

                        userCurrencyAssetRespList.add(userCurrencyAssetResp);
                    }
                }

                // 设置币种个数
                userExchangeAssetResp.setCurrencyCount(userCurrencyList.size());

                // 添加三级数据
                userExchangeAssetResp.setUserCurrencyAssetRespList(userCurrencyAssetRespList);

                userExchangeAssetRespList.add(userExchangeAssetResp);
            }



            // 添加二级数据
            assetResp.setUserExchangeAssetRespList(userExchangeAssetRespList);

            // 聚合数据格式
            // rest
            //    asset
            //        exchangeList
            //            currencyList

            // 添加一级数据
            map.put("asset", assetResp);

            return RESTful.Success(map);

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

            // 验证token
            if (StringUtils.isEmpty(token.trim())) {
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
            Exchange exchange = exchangeMapper.selectEnabledByShortName(assetSyn.getExchangeName());
            if (exchange == null) {
                return RESTful.Fail(CodeEnum.ExchangeInvalid);
            }

            Map<String, Object> typeMapper = new HashMap<>();
            typeMapper.put("userId", user.getId());
            typeMapper.put("exchangeName", assetSyn.getExchangeName());
            UserApi userApi = userApiMapper.selectEnabledByType(typeMapper);
            if (userApi == null) {
                // 请勿重复授权
                return RESTful.Fail(CodeEnum.NotExistAuthOfExchange);
            }

            switch (assetSyn.getExchangeName()) {
                case "huobi": synHuoBi(user.getId(), userApi.getApiKey(), userApi.getApiSecret()); break;
                default: break;
            }

            return RESTful.Success();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return RESTful.SystemException();
        }
    }

    private void synHuoBi(Integer userId, String apiKey, String apiSecret) throws Exception {

        Date date = new Date();

        // 获取用户信息，保存数据库
        IApiClient apiClient = new HuoBiApiClient(apiKey, apiSecret);
        String respBody = apiClient.accounts();
        ApiResp apiResp = objectMapper.readValue(respBody, ApiResp.class);
        if ("ok".equals(apiResp.getStatus())) {

            List<UserCurrency> userCurrencyList = new LinkedList<>();

            Account account = objectMapper.convertValue(apiResp.getData(), Account.class);
            List<Balance> balanceList = objectMapper.convertValue(account.getList(), new TypeReference<List<Balance>>() { });
            // 非安全
            for (Balance balance:balanceList) {
                // 存储
                UserCurrency userCurrency = new UserCurrency();
                if (Double.valueOf(balance.getBalance()) > 0) {
                    userCurrency.setExchangeName("huobi");
                    userCurrency.setCurrency(balance.getCurrency());
                    userCurrency.setType(balance.getType());
                    userCurrency.setBalance(balance.getBalance());
                    userCurrency.setUserId(userId);
                    userCurrency.setApiKey(apiKey);
                    userCurrency.setApiSecret(apiSecret);
                    userCurrency.setAccountId(account.getId().toString());
                    userCurrency.setCreateTime(date);
                    userCurrencyList.add(userCurrency);
                }

//                else {
//                    if ("btc".equals(balance.getCurrency())) {
//                        userCurrency.setExchangeName("huobi");
//                        userCurrency.setCurrency(balance.getCurrency());
//                        userCurrency.setType(balance.getType());
//                        userCurrency.setBalance(balance.getBalance());
//                        userCurrency.setUserId(userId);
//                        userCurrency.setApiKey(apiKey);
//                        userCurrency.setApiSecret(apiSecret);
//                        userCurrency.setAccountId(account.getId().toString());
//                        userCurrency.setCreateTime(date);
//                        userCurrencyList.add(userCurrency);
//                    }
//                }
            }

            saveUserAsset(apiKey, userCurrencyList);
        }
    }

    @Transactional
    void saveUserAsset(String apiKey, List<UserCurrency> userCurrencyList) {

        userCurrencyMapper.deleteByApiKey(apiKey);
        for (UserCurrency userCurrency : userCurrencyList) {
            userCurrencyMapper.insert(userCurrency);
        }
    }
}
