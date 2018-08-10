package com.sharex.token.api.currency.okex;

import com.sharex.token.api.currency.IApiClient;
import com.sharex.token.api.exception.ApiNotExistException;
import com.sharex.token.api.util.CryptoUtil;
import com.sharex.token.api.util.HttpUtil;
import com.sharex.token.api.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OkexApiClient implements IApiClient {

    private static final String API_HOST = "www.okex.com";
    private static final String API_URL = "https://" + API_HOST;

//    private String apiKey;
//    private String apiSecret;

    // 最新成交
    private static final String Trades_URL = "/api/v1/trades.do";

    // 行情--替换kline--1min数据第一条
    private static final String TICKER_URL = "/api/v1/ticker.do";

    // kline
    private static final String Kline_URL = "/api/v1/kline.do";

    // 账户
    private static final String Accounts_URL = "/api/v1/userinfo.do?";

    // 用户历史订单
    private static final String HistoryOrders_URL = "/api/v1/order_history.do";

    // 用户委托查询（部分平台 委托 与 订单 在一个接口返回）
    private static final String Orders_URL = "";

    // 下单
    private static final String PlaceOrder_URL = "/api/v1/trade.do?";

    private static final String Cancel_URL = "/api/v1/cancel_order";

//    public OkexApiClient(String apiKey, String apiSecret) {
//        this.apiKey = apiKey;
//        this.apiSecret = apiSecret;
//    }

    public OkexApiClient() {

    }

    /**
     * 600条，需要裁剪数据存入redis
     * @param symbol
     * @param size
     * @return
     * @throws Exception
     */
    @Override
    public String trades(String symbol, Integer size) throws Exception {

        Map<String, String> map = new HashMap<>();
        map.put("symbol", symbol);

        String queryString = StringUtil.toQueryString(map);

        String respBody = HttpUtil.get(API_URL + Trades_URL + "?" + queryString, null);

        return respBody;
    }

    @Override
    public String ticker(String symbol) throws Exception {

        Map<String, String> map = new HashMap<>();
        map.put("symbol", symbol);

        String queryString = StringUtil.toQueryString(map);

        String respBody = HttpUtil.get(API_URL + TICKER_URL + "?" + queryString, null);

        return respBody;
    }

    @Override
    public String kline(String symbol, String type, Integer size) throws Exception {

        //[
        //    [
        //        1417449600000,
        //        2339.11,
        //        2383.15,
        //        2322,
        //        2369.85,
        //        83850.06
        //    ],
        //    [
        //        1417536000000,
        //        2370.16,
        //        2380,
        //        2352,
        //        2367.37,
        //        17259.83
        //    ]
        //]

        Map<String, String> map = new HashMap<>();
        map.put("symbol", symbol);
        map.put("type", type);
        if (size > 0) {
            map.put("size", size.toString());
        }

        String queryString = StringUtil.toQueryString(map);

        String respBody = HttpUtil.get(API_URL + Kline_URL + "?" + queryString, null);

        return respBody;
    }

    @Override
    public String accounts(String apiKey, String apiSecret) throws Exception {

        // 构造参数签名
        Map<String, String> params = new HashMap<String, String>();
        params.put("api_key", apiKey);
        String sign = CryptoUtil.md5(StringUtil.toQueryString(params) + "&secret_key=" + apiSecret);
        params.put("sign", sign);

        // 发送post请求
        String respBody = HttpUtil.post(API_URL + Accounts_URL,
                StringUtil.toQueryString(params), "application/x-www-form-urlencoded");

        return respBody;
    }

    @Override
    public String openOrders(String apiKey, String apiSecret, String accountId, String symbol, Integer status, Integer size) throws Exception {

        Map<String, String> params = new HashMap<String, String>();
        params.put("api_key", apiKey);
        params.put("symbol", symbol);
        params.put("status", status.toString());
        params.put("current_page", "1");
        params.put("page_length", "100");
        String sign = CryptoUtil.md5(StringUtil.toQueryString(params) + "&secret_key=" + apiSecret);
        params.put("sign", sign);

        // 发送post请求
        String respBody = HttpUtil.post(API_URL + this.HistoryOrders_URL,
                StringUtil.toQueryString(params),"application/x-www-form-urlencoded");

        return respBody;
    }

    @Override
    public String historyOrders(String apiKey, String apiSecret, String accountId, String symbol, Integer status, Integer size) throws Exception {

        Map<String, String> params = new HashMap<String, String>();
        params.put("api_key", apiKey);
        params.put("symbol", symbol);
        params.put("status", status.toString());
        params.put("current_page", "1");
        params.put("page_length", size.toString());
        String sign = CryptoUtil.md5(StringUtil.toQueryString(params) + "&secret_key=" + apiSecret);
        params.put("sign", sign);

        // 发送post请求
        String respBody = HttpUtil.post(API_URL + this.HistoryOrders_URL,
                StringUtil.toQueryString(params),"application/x-www-form-urlencoded");

        return respBody;
    }

    @Override
    public String entrustOrders(String apiKey, String apiSecret, String symbol) throws Exception {

        throw new ApiNotExistException("okex entrust orders interface not exist, please see history orders");
    }

    /**
     * 下单 - 仅支持限价
     * @param accountId
     * @param symbol
     * @param price
     * @param amount
     * @param type
     * @return
     * @throws Exception
     */
    @Override
    public String placeOrder(String apiKey, String apiSecret, String accountId, String symbol, String price, String amount, String type) throws Exception {
        // 构造参数签名
        Map<String, String> params = new HashMap<String, String>();
        params.put("api_key", apiKey);
        if(!StringUtils.isEmpty(symbol)){
            params.put("symbol", symbol);
        }
        if(!StringUtils.isEmpty(type)){
            params.put("type", type);
        }
        if(!StringUtils.isEmpty(price)){
            params.put("price", price);
        }
        if(!StringUtils.isEmpty(amount)){
            params.put("amount", amount);
        }
        String sign = CryptoUtil.md5(StringUtil.toQueryString(params) + "&secret_key=" + apiSecret);
        params.put("sign", sign);

        // 发送post请求
        String respBody = HttpUtil.post(API_URL + this.PlaceOrder_URL,
                StringUtil.toQueryString(params), "application/x-www-form-urlencoded");

        return respBody;
    }

    @Override
    public String cancelOrder(String apiKey, String apiSecret, String symbol, String orderId) throws Exception {

        Map<String, String> params = new HashMap<String, String>();
        params.put("api_key", apiKey);
        if(!StringUtils.isEmpty(symbol)){
            params.put("symbol", symbol);
        }
        if(!StringUtils.isEmpty(orderId)){
            params.put("order_id", orderId);
        }
        String sign = CryptoUtil.md5(StringUtil.toQueryString(params) + "&secret_key=" + apiSecret);
        params.put("sign", sign);

        // 发送post请求
        String respBody = HttpUtil.post(API_URL + this.Cancel_URL,
                StringUtil.toQueryString(params), "application/x-www-form-urlencoded");

        return respBody;
    }
}
