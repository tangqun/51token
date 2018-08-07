package com.sharex.token.api.currency;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface IApiClient {

    /**
     * 最新成交
     * @return
     * @throws Exception
     */
    String trades(String symbol, Integer size) throws Exception;

    /**
     * 行情（单个Symbol）
     * @param symbol
     * @return
     */
    String ticker(String symbol) throws Exception;

    /**
     * 深度（单个Symbol）
     * @param symbol
     * @return
     */
//    String depth(String symbol);

    /**
     * kline
     * @param symbol
     * @param type
     * @param size
     * @return
     */
    String kline(String symbol, String type, Integer size) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, Exception;

    /************************************************* 币币交易 ****************************************************/

    /**
     * 获取账户信息
     * @return
     */
    String accounts(String apiKey, String apiSecret) throws Exception;

    /**
     * 未成交订单
     * @param accountId
     * @param symbol
     * @return
     * @throws Exception
     */
    String openOrders(String apiKey, String apiSecret, String accountId, String symbol, Integer status, Integer size) throws Exception;

    /**
     * 用户历史成交（已成交 & 未成交 ？）
     * @param symbol
     * @param status
     * @return
     */
    String historyOrders(String apiKey, String apiSecret, String accountId, String symbol, Integer status, Integer size) throws Exception;

    /**
     * 委托
     * @param symbol
     * @return
     */
    String entrustOrders(String apiKey, String apiSecret, String symbol) throws Exception;

    /**
     * 下单 - 限价买卖
     * @param symbol
     * @param price
     * @param amount
     * @param type
     * @return
     */
    String placeOrder(String apiKey, String apiSecret, String accountId, String symbol, String price, String amount, String type) throws Exception;

    /**
     * 撤销订单
     * @param symbol
     * @param orderId
     * @return
     */
//    String cancelOrder(String symbol, String orderId);

    /**
     * 查询订单
     * @param symbol
     * @param orderId
     * @return
     */
//    String queryOrder(String symbol, String orderId);
}
