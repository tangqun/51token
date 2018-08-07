package com.sharex.token.api.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharex.token.api.currency.resolver.HuoBiApiResolver;
import com.sharex.token.api.currency.resolver.IApiResolver;
import com.sharex.token.api.currency.resolver.OkexApiResolver;
import com.sharex.token.api.entity.*;
import com.sharex.token.api.exception.NetworkException;
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
    private HuoBiApiResolver huoBiApiResolver;

    @Autowired
    private OkexApiResolver okexApiResolver;

    private ObjectMapper objectMapper = new ObjectMapper();

    private IApiResolver getApiResolver(String exchangeName) {

        IApiResolver apiResolver = null;
        switch (exchangeName) {
            case "huobi": apiResolver = huoBiApiResolver; break;

//            case "okex": return new
        }
        return apiResolver;
    }

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

        try {

            IApiResolver apiResolver = getApiResolver(exchangeName);

            //            IApiResolver apiResolver = ApiResolverFactory.getInstence(exchangeName);

            if (null != apiResolver) {

                RemoteSyn<List<MyKline>> remoteSyn = apiResolver.getKline(symbol, type);

                hashOperations.put(exchangeName, "kline_" + symbol + "_" + type, objectMapper.writeValueAsString(remoteSyn));

                return remoteSyn.getData();
            }

            // 抛出异常
            throw new NetworkException();

        } catch (Exception e) {

            // 启动的时候 拉取一次 行情数据，减少异常的可能性
            if (!StringUtils.isBlank(redisBody)) {
                RemoteSyn<List<MyKline>> remoteSyn_redis = objectMapper.readValue(redisBody, new TypeReference<RemoteSyn<List<MyKline>>>() { });

                return remoteSyn_redis.getData();
            } else {

                // 两次抛出异常
                IApiResolver apiResolver = getApiResolver(exchangeName);

                RemoteSyn<List<MyKline>> remoteSyn = apiResolver.getKline(symbol, type);

                hashOperations.put(exchangeName, "kline_" + symbol + "_" + type, objectMapper.writeValueAsString(remoteSyn));

                return remoteSyn.getData();
            }
        }
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

        IApiResolver apiResolver = getApiResolver(exchangeName);

        RemoteSyn<MyTrades> remoteSyn = apiResolver.getTrades(symbol);

        hashOperations.put(exchangeName, "trades_" + symbol, objectMapper.writeValueAsString(remoteSyn));

        return remoteSyn.getData();
    }

    public void synAccounts(String exchangeName, Integer userId, String apiKey, String apiSecret) throws Exception {

        IApiResolver apiResolver = getApiResolver(exchangeName);

        Map<String, UserCurrency> map = apiResolver.accounts(apiKey, apiSecret, userId);

        for (Map.Entry<String, UserCurrency> entry:map.entrySet()) {

            if (!"usdt".equals(entry.getKey())) {

                String symbol = "";
                switch (exchangeName) {
                    case "huobi": symbol = entry.getKey() + "usdt"; break;
                    case "okex": symbol = entry.getKey() + "_usdt"; break;
                }


                List<MyKline> myKlineList = getKline(exchangeName, symbol, "1min");
                MyKline myKline = myKlineList.get(0);

                entry.getValue().setCost(myKline.getClose());
            } else {

                entry.getValue().setCost(entry.getValue().getFree());
            }
        }

        saveUserAsset(exchangeName, userId, map);
    }

    public void synOpenOrders(String apiKey, String apiSecret, String accountId, Integer userId, String exchangeName, String symbol) throws Exception {

        // huobi
        //   openOrders_btcusdt_userId
        String redisBody = hashOperations.get(exchangeName,  "openOrders_" + symbol + "_" + userId);

        // 解析，判断时间戳
        if (!StringUtils.isBlank(redisBody)) {
            RemoteSyn<List<MyOpenOrders>> remoteSyn_redis = objectMapper.readValue(redisBody, new TypeReference<RemoteSyn<List<MyOpenOrders>>>() {});

            // redis 数据有效，直接返回
            if (System.currentTimeMillis() - remoteSyn_redis.getTs() <= 5000) {

                return;
            }
        }

        IApiResolver apiResolver = getApiResolver(exchangeName);

        RemoteSyn<MyOpenOrders> remoteSyn = apiResolver.getOpenOrders(apiKey, apiSecret, accountId, symbol, 0, 100);

        hashOperations.put(exchangeName, "openOrders_" + symbol + "_" + userId, objectMapper.writeValueAsString(remoteSyn));
    }

    public void synHistoryOrders(String apiKey, String apiSecret, String accountId, Integer userId, String exchangeName, String symbol) throws Exception {

        String redisBody = hashOperations.get(exchangeName,  "historyOrders_" + symbol + "_" + userId);

        if (!StringUtils.isBlank(redisBody)) {
            RemoteSyn<List<MyHistoryOrders>> remoteSyn_redis = objectMapper.readValue(redisBody, new TypeReference<RemoteSyn<List<MyHistoryOrders>>>() {});

            // redis 数据有效，直接返回
            if (System.currentTimeMillis() - remoteSyn_redis.getTs() <= 5000) {

                return;
            }
        }

        IApiResolver apiResolver = getApiResolver(exchangeName);

        RemoteSyn<MyHistoryOrders> remoteSyn = apiResolver.getHistoryOrders(apiKey, apiSecret, accountId, symbol, 0, 100);

        hashOperations.put(exchangeName, "historyOrders_" + symbol + "_" + userId, objectMapper.writeValueAsString(remoteSyn));
    }

    public RemotePost<String> placeOrder(String exchangeName, Integer userId, String apiKey, String apiSecret, String accountId, String symbol, String price, String amount, String type) throws Exception {

        IApiResolver apiResolver = getApiResolver(exchangeName);

        return apiResolver.placeOrder(apiKey, apiSecret, accountId, symbol, price, amount, type);
    }

    public RemotePost<String> cancelOrder(String exchangeName, Integer userId, String apiKey, String apiSecret, String symbol, String orderId) throws Exception {

        IApiResolver apiResolver = getApiResolver(exchangeName);

        return apiResolver.cancelOrder(apiKey, apiSecret, symbol, orderId);
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
