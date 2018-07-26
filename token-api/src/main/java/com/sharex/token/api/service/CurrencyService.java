package com.sharex.token.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharex.token.api.currency.IApiClient;
import com.sharex.token.api.currency.huobi.HuoBiApiClient;
import com.sharex.token.api.entity.*;
import com.sharex.token.api.entity.enums.CodeEnum;
import com.sharex.token.api.mapper.ExchangeMapper;
import com.sharex.token.api.mapper.UserApiMapper;
import com.sharex.token.api.mapper.UserMapper;
import com.sharex.token.api.util.ValidateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
    private HashOperations<String, String, Object> hashOperations;

    private ObjectMapper objectMapper = new ObjectMapper();

    public RESTful getAccounts(String token, String exchangeName) {
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

                IApiClient apiClient = ApiFactory.getApiClient(exchangeName, userApi.getApiKey(), userApi.getApiSecret());
                apiClient.accounts();

                return null;
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
