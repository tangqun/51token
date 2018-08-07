package com.sharex.token.api.currency.binance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharex.token.api.currency.IApiClient;
import com.sharex.token.api.util.CryptoUtil;
import com.sharex.token.api.util.HttpUtil;
import com.sharex.token.api.util.StringUtil;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class BinanceApiClient implements IApiClient {

    // https://github.com/binance-exchange/binance-official-api-docs/blob/master/rest-api.md

    // The /api/v1/exchangeInfo rateLimits array contains objects related to the exchange's REQUEST_WEIGHT and ORDER rate limits.
    // Each route has a weight which determines for the number of requests each endpoint counts for. Heavier endpoints and endpoints that do operations on multiple symbols will have a heavier weight.

    // 特别注意
    // A 429 will be returned when either rate limit is violated.
    // When a 429 is recieved, it's your obligation as an API to back off and not spam the API.
    // Repeatedly violating rate limits and/or failing to back off after receiving 429s will result in an automated IP ban (http status 418).
    // IP bans are tracked and scale in duration for repeat offenders, from 2 minutes to 3 days.

    private static final String API_HOST = "api.binance.com";
    private static final String API_URL = "https://" + API_HOST;
    private static final DateTimeFormatter DT_FORMAT = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss");
    private static final ZoneId ZONE_GMT = ZoneId.of("Z");

//    private String apiKey;
//    private String apiSecret;

    // 最新成交
    private static final String Trades_URL = "/api/v1/trades";

    // 行情
    private static final String Ticker_URL = "/api/v1/aggTrades";

    // kline
    private static final String Kline_URL = "/api/v1/klines";

    // 账户
    private static final String Accounts_URL = "/api/v3/account";

    // 用户历史订单
    private static final String HistoryOrders_URL = "/api/v3/myTrades";

    // 用户委托查询（部分平台 委托 与 订单 在一个接口返回）
    private static final String Orders_URL = "";

    // 下单
    private static final String PlaceOrder_URL = "";

    private ObjectMapper objectMapper = new ObjectMapper();

//    public BinanceApiClient(String apiKey, String apiSecret) {
//        this.apiKey = apiKey;
//        this.apiSecret = apiSecret;
//    }

    public BinanceApiClient() {

    }

    @Override
    public String trades(String symbol, Integer size) throws Exception {

        Map<String, String> map = new HashMap<>();
        map.put("symbol", symbol);
        if (size > 0) {
            map.put("limit", size.toString());
        }

        String queryString = StringUtil.toQueryString(map);

        String respBody = HttpUtil.get(API_URL + Trades_URL + "?" + queryString, null);

        return respBody;
    }

    @Override
    public String ticker(String symbol) throws Exception {

        Map<String, String> map = new HashMap<>();
        map.put("symbol", symbol);
        map.put("limit", "1");

        String queryString = StringUtil.toQueryString(map);

        String respBody = HttpUtil.get(API_URL + Ticker_URL + "?" + queryString, null);

        return respBody;
    }

    @Override
    public String kline(String symbol, String type, Integer size) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, Exception {

        //1m
        //3m
        //5m
        //15m
        //30m
        //1h
        //2h
        //4h
        //6h
        //8h
        //12h
        //1d
        //3d
        //1w
        //1M

        //[
        //  [
        //    1499040000000,      // Open time
        //    "0.01634790",       // Open
        //    "0.80000000",       // High
        //    "0.01575800",       // Low
        //    "0.01577100",       // Close
        //    "148976.11427815",  // Volume
        //    1499644799999,      // Close time
        //    "2434.19055334",    // Quote asset volume
        //    308,                // Number of trades
        //    "1756.87402397",    // Taker buy base asset volume
        //    "28.46694368",      // Taker buy quote asset volume
        //    "17928899.62484339" // Ignore.
        //  ]
        //]

        Map<String, String> map = new HashMap<>();
        map.put("symbol", symbol);
        map.put("interval", type);
        if (size > 0) {
            map.put("limit", size.toString());
        }

        String queryString = StringUtil.toQueryString(map);

        String respBody = HttpUtil.get(API_URL + Kline_URL + "?" + queryString, null);

        return respBody;
    }

    @Override
    public String accounts(String apiKey, String apiSecret) throws Exception {

        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("recvWindow", "5000");
        String queryString = StringUtil.toQueryString(map);
        String signature = CryptoUtil.hmacSha256(apiSecret, queryString);
        map.put("signature", signature.toLowerCase());
        queryString = StringUtil.toQueryString(map);

        Map<String, String> headers = new HashMap<>();
        headers.put("X-MBX-APIKEY", apiKey);

        String respBody = HttpUtil.get(API_URL + Accounts_URL + "?" + queryString, headers);

        return respBody;
    }

    @Override
    public String openOrders(String apiKey, String apiSecret, String accountId, String symbol, Integer status, Integer size) throws Exception {
        return null;
    }

    @Override
    public String historyOrders(String apiKey, String apiSecret, String accountId, String symbol, Integer status, Integer size) throws Exception {

        Map<String, String> map = new HashMap<>();
        map.put("symbol", symbol);
        // 暂定100条
        map.put("limit", "100");
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("recvWindow", "5000");
        String queryString = StringUtil.toQueryString(map);
        String signature = CryptoUtil.hmacSha256(apiSecret, queryString);
        map.put("signature", signature.toLowerCase());
        queryString = StringUtil.toQueryString(map);

        Map<String, String> headers = new HashMap<>();
        headers.put("X-MBX-APIKEY", apiKey);

        String respBody = HttpUtil.get(API_URL + HistoryOrders_URL + "?" + queryString, headers);

        return respBody;
    }

    @Override
    public String entrustOrders(String apiKey, String apiSecret, String symbol) throws Exception {
        return null;
    }

    @Override
    public String placeOrder(String apiKey, String apiSecret, String accountId, String symbol, String price, String amount, String type) throws Exception {
        return null;
    }
}
