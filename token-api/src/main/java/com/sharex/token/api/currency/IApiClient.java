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
     * 聚合行情（单个Symbol）
     * @param symbol
     * @return
     */
    String ticker(String symbol) throws Exception;

    /**
     * 市场深度（单个Symbol）
     * @param symbol
     * @return
     */
//    String depth(String symbol);

    /**
     * 历史交易记录
     * @param symbol
     * @param size 1~200
     * @return
     */
//    String historyTrades(String symbol, Integer size);

    /**
     * k线
     * @param symbol
     * @param type
     * @param size
     * @return
     */
    String kline(String symbol, String type, Integer size) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, Exception;

    /*********************** 币币交易 **************************/

    /**
     * 获取账户信息
     * @return
     */
    String accounts() throws Exception;

    /**
     * 下单 - 限价买卖
     * @param symbol
     * @param price
     * @param amount
     * @param type
     * @return
     */
    String placeOrder(String accountId, String symbol, String price, String amount, String type) throws Exception;

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

    /**
     * 用户历史订单（已成交 & 未成交 ？）
     * @param symbol
     * @param status
     * @return
     */
    String historyOrders(String symbol, Integer status) throws Exception;

    /**
     * 委托
     * @param symbol
     * @return
     */
    String entrustOrders(String symbol) throws Exception;
}
