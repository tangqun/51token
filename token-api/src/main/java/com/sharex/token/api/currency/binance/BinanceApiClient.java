package com.sharex.token.api.currency.binance;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class BinanceApiClient {

    // https://github.com/binance-exchange/binance-official-api-docs/blob/master/rest-api.md

    // The /api/v1/exchangeInfo rateLimits array contains objects related to the exchange's REQUEST_WEIGHT and ORDER rate limits.
    // Each route has a weight which determines for the number of requests each endpoint counts for. Heavier endpoints and endpoints that do operations on multiple symbols will have a heavier weight.

    // 特别注意
    // A 429 will be returned when either rate limit is violated.
    // When a 429 is recieved, it's your obligation as an API to back off and not spam the API.
    // Repeatedly violating rate limits and/or failing to back off after receiving 429s will result in an automated IP ban (http status 418).
    // IP bans are tracked and scale in duration for repeat offenders, from 2 minutes to 3 days.

    private static final String API_HOST = "api.huobipro.com";
    private static final String API_URL = "https://" + API_HOST;
    private static final DateTimeFormatter DT_FORMAT = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss");
    private static final ZoneId ZONE_GMT = ZoneId.of("Z");

    private final String apiKey;
    private final String apiSecret;

    // 成交记录
    private static final String Trades_URL = "/market/history/trade";

    // 行情
    private static final String Ticker_URL = "/market/detail/merged";

    // 账户
    private static final String Accounts_URL = "/v1/account/accounts";
    private static final String AccountsBalance_URL = "/v1/account/accounts/%s/balance";

    // 委托查询
    private static final String Orders_URL = "/v1/order/orders";

    // 用户历史成交
    private static final String HistoryOrders_URL = "/v1/order/matchresults";

    // 下单
    private static final String PlaceOrder_URL = "/v1/order/orders/place";

    // kline
    private static final String Kline_URL = "/market/history/kline";

    private ObjectMapper objectMapper = new ObjectMapper();

    public BinanceApiClient(String apiKey, String apiSecret) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }
}
