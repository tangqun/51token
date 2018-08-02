package com.sharex.token.api.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharex.token.api.currency.resolver.ApiResolverFactory;
import com.sharex.token.api.currency.resolver.IApiResolver;
import com.sharex.token.api.entity.MyKline;
import com.sharex.token.api.entity.MyTrades;
import com.sharex.token.api.entity.RemoteSyn;
import com.sharex.token.api.entity.UserCurrency;
import com.sharex.token.api.mapper.UserCurrencyMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class RemoteSynService {

    private static final Log logger = LogFactory.getLog(RemoteSynService.class);

    @Autowired
    private HashOperations<String, String, String> hashOperations;

    @Autowired
    private UserCurrencyMapper userCurrencyMapper;

    @Autowired
    private IApiResolver apiResolver;

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 获取k线数据
     * @param symbol
     * @param type 1min 15min 30min 1hour 1day 1week
     * @return
     */
    public List<MyKline> getKline(String exchangeName, String symbol, String type) throws Exception {

        String redisBody = hashOperations.get(exchangeName,  "kline_" + symbol + "_1min");

        // 解析，判断时间戳
        if (!StringUtils.isBlank(redisBody)) {
            RemoteSyn<List<MyKline>> remoteSyn_redis = objectMapper.readValue(redisBody, new TypeReference<RemoteSyn<List<MyKline>>>() {});

            // redis 数据有效，直接返回
            if (System.currentTimeMillis() - remoteSyn_redis.getTs() <= 5000) {

                return remoteSyn_redis.getData();
            }
        }

        IApiResolver apiResolver = ApiResolverFactory.getInstence(exchangeName);

        RemoteSyn<List<MyKline>> remoteSyn = apiResolver.getKline(symbol, type);

        hashOperations.put(exchangeName, "kline_" + symbol + "_" + type, objectMapper.writeValueAsString(remoteSyn));

        return remoteSyn.getData();
    }

    public MyTrades getTrades(String exchangeName, String symbol) throws Exception {

        String redisBody = hashOperations.get(exchangeName,  "trades_" + symbol);

        // 解析，判断时间戳
        if (!StringUtils.isBlank(redisBody)) {
            RemoteSyn<MyTrades> remoteSyn_redis = objectMapper.readValue(redisBody, new TypeReference<RemoteSyn<MyTrades>>() {});

            // redis 数据有效，直接返回
            if (System.currentTimeMillis() - remoteSyn_redis.getTs() <= 5000) {

                return remoteSyn_redis.getData();
            }
        }

        IApiResolver apiResolver = ApiResolverFactory.getInstence(exchangeName);

        RemoteSyn<MyTrades> remoteSyn = apiResolver.getTrades(symbol);

        hashOperations.put(exchangeName, "trades_" + symbol, objectMapper.writeValueAsString(remoteSyn));

        return remoteSyn.getData();
    }

    public void synAccounts(String exchangeName, Integer userId, String apiKey, String apiSecret) throws Exception {

        IApiResolver apiResolver = ApiResolverFactory.getInstence2(exchangeName, apiKey, apiSecret);

        Map<String, UserCurrency> map = apiResolver.accounts(userId);

        saveUserAsset(exchangeName, userId, map);
    }

    @Transactional
    void saveUserAsset(String exchangeName, Integer userId, Map<String, UserCurrency> map) {

        Map<String, Object> deleteMap = new HashMap<>();
        deleteMap.put("exchangeName", exchangeName);
        deleteMap.put("userId", userId);
        userCurrencyMapper.delete(deleteMap);

        Set<Map.Entry<String, UserCurrency>> entrySet = map.entrySet();
        for (Map.Entry<String, UserCurrency> entry:entrySet) {
            userCurrencyMapper.insert(entry.getValue());
        }
    }
}
