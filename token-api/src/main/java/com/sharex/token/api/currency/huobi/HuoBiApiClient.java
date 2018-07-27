package com.sharex.token.api.currency.huobi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharex.token.api.currency.IApiClient;
import com.sharex.token.api.currency.huobi.resp.Accounts;
import com.sharex.token.api.currency.huobi.resp.ApiResp;
import com.sharex.token.api.util.CryptoUtil;
import com.sharex.token.api.util.HttpUtil;
import com.sharex.token.api.util.StringUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class HuoBiApiClient implements IApiClient {

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

    public HuoBiApiClient(String apiKey, String apiSecret) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    /**
     * 最新成交
     * @param symbol
     * @param size
     * @return
     * @throws Exception
     */
    @Override
    public String trades(String symbol, Integer size) throws Exception {

        Map<String, String> map = new HashMap<>();
        map.put("symbol", symbol);
        if (size > 0) {
            map.put("size", size.toString());
        }

        String queryString = toQueryString("GET", API_HOST, Trades_URL, map);

        String respBody = HttpUtil.get(API_URL + Trades_URL + "?" + queryString);

        return respBody;
    }

    @Override
    public String ticker(String symbol) throws Exception {

        String queryString = toQueryString("GET", API_HOST, Ticker_URL, new HashMap<>());

        String respBody = HttpUtil.get(API_URL + Ticker_URL + "?" + queryString);

        return respBody;
    }

    @Override
    public String kline(String symbol, String type, Integer size) throws Exception {

        Map<String, String> map = new HashMap<>();
        map.put("symbol", symbol);
        map.put("type", type);
        if (size > 0) {
            map.put("size", size.toString());
        }

        String queryString = toQueryString("GET", API_HOST, Kline_URL, map);

        String respBody = HttpUtil.get(API_URL + Kline_URL + "?" + queryString);

        return respBody;
    }

    @Override
    public String accounts() throws Exception {

        String queryString = toQueryString("GET", API_HOST, Accounts_URL, new HashMap<>());

        String respBody = HttpUtil.get(API_URL + Accounts_URL + "?" + queryString);

        ApiResp apiResp = objectMapper.readValue(respBody, ApiResp.class);
        if ("ok".equals(apiResp.getStatus())) {

            List<Accounts> accountsList = objectMapper.convertValue(apiResp.getData(), new TypeReference<List<Accounts>>() { });
            return account(accountsList.get(0).getId());
        }

        return null;
    }

    private String account(Long accountId) throws Exception {

        String uri = String.format(AccountsBalance_URL, accountId);
        String queryString = toQueryString("GET", API_HOST, uri, new HashMap<>());

        String respBody = HttpUtil.get(API_URL + uri + "?" + queryString);

        return respBody;
    }

    /**
     * 下单
     * @param symbol
     * @param price
     * @param amount
     * @param type
     * @return
     */
    @Override
    public String placeOrder(String accountId, String symbol, String price, String amount, String type) throws Exception {

        Map<String, String> map = new HashMap<>();
        map.put("account-id", accountId);
        map.put("amount", amount);
        map.put("price", price);
        map.put("symbol", symbol);
        map.put("type", type);
        String queryString = toQueryString("POST", API_HOST, PlaceOrder_URL, new HashMap<>());

        String respBody = HttpUtil.post(API_URL + PlaceOrder_URL + "?" + queryString, objectMapper.writeValueAsString(map), "application/json");

        return respBody;
    }

    @Override
    public String historyOrders(String symbol, Integer status) throws Exception {

        Map<String, String> map = new HashMap<>();
        map.put("symbol", symbol);
        // buy-market：市价买, sell-market：市价卖, buy-limit：限价买, sell-limit：限价卖, buy-ioc：IOC买单, sell-ioc：IOC卖单
        map.put("types", "buy-market,sell-market,buy-limit,sell-limit");

        String queryString = toQueryString("GET", API_HOST, HistoryOrders_URL, map);

        String respBody = HttpUtil.get(API_URL + HistoryOrders_URL + "?" + queryString);

        return respBody;
    }

    /**
     * 委托查询
     */
    public String entrustOrders(String symbol) throws Exception {

        Map<String, String> map = new HashMap<>();
        map.put("symbol", symbol);
        map.put("states", "submitted,partial-filled,partial-canceled,filled,canceled");
        map.put("size", "200");
        String queryString = toQueryString("GET", API_HOST, Orders_URL, map);

        String respBody = HttpUtil.get(API_URL + Orders_URL + "?" + queryString);

        return respBody;
    }

    private String toQueryString(String method, String host, String uri, Map<String, String> params) throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {

        StringBuilder sb = new StringBuilder(1024);
        sb.append(method.toUpperCase()).append('\n') // GET
                .append(host.toLowerCase()).append('\n') // Host
                .append(uri).append('\n'); // /path
        params.remove("Signature");
        params.put("AccessKeyId", apiKey);
        params.put("SignatureVersion", "2");
        params.put("SignatureMethod", "HmacSHA256");
        params.put("Timestamp", gmtNow());
        // build signature:
        SortedMap<String, String> map = new TreeMap<>(params);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append('=').append(urlEncode(value)).append('&');
        }
        // remove last '&':
        sb.deleteCharAt(sb.length() - 1);

        String sign = CryptoUtil.hmacSha256(apiSecret, sb.toString());
        params.put("Signature", sign);

        return StringUtil.toQueryString(params);
    }

    /**
     * 使用标准URL Encode编码。注意和JDK默认的不同，空格被编码为%20而不是+。
     *
     * @param s String字符串
     * @return URL编码后的字符串
     */
    public static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("UTF-8 encoding not supported!");
        }
    }

    /**
     * Return epoch seconds
     */
    long epochNow() {
        return Instant.now().getEpochSecond();
    }

    String gmtNow() {
        return Instant.ofEpochSecond(epochNow()).atZone(ZONE_GMT).format(DT_FORMAT);
    }
}
