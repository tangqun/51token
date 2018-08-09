package com.sharex.token.api.currency.resolver;

import com.sharex.token.api.entity.RemotePost;
import com.sharex.token.api.entity.RemoteSyn;
import com.sharex.token.api.entity.UserCurrency;

import java.util.Map;

public interface IApiResolver {

    RemoteSyn getKline(String symbol, String type) throws Exception;

    RemoteSyn getTrades(String symbol) throws Exception;

    Boolean accounts(String apiKey, String apiSecret) throws Exception;

    Map<String, UserCurrency> accounts(String apiKey, String apiSecret, Integer userId) throws Exception;

    RemotePost<String> placeOrder(String apiKey, String apiSecret, String accountId, String symbol, String price, String amount, String type) throws Exception;

    RemotePost<String> cancelOrder(String apiKey, String apiSecret, String symbol, String orderId) throws Exception;

    RemoteSyn getOpenOrders(String apiKey, String apiSecret, String accountId, String symbol, Integer status, Integer size) throws Exception;

    RemoteSyn getHistoryOrders(String apiKey, String apiSecret, String accountId, String symbol, Integer status, Integer size) throws Exception;
}
