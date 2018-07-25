package com.sharex.token.api.currency.okex;

import com.sharex.token.api.currency.IApiClient;
import com.sharex.token.api.util.CryptoUtil;
import com.sharex.token.api.util.HttpUtil;
import com.sharex.token.api.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class OkexApiClient implements IApiClient {

    private static final String API_HOST = "www.okex.com";
    private static final String API_URL = "https://" + API_HOST;

    private final String apiKey;
    private final String apiSecret;

    // 行情
    private final String TICKER_URL = "/api/v1/ticker.do?";

    // 用户信息
    private final String USERINFO_URL = "/api/v1/userinfo.do?";

    // 下单
    private final String TRADE_URL = "/api/v1/trade.do?";

    // 历史订单
    private final String ORDER_HISTORY_URL = "/api/v1/order_history.do";

    public OkexApiClient(String apiKey, String apiSecret) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    @Override
    public String ticker(String symbol) throws Exception {

        String respBody = HttpUtil.get(API_URL + TICKER_URL + "symbol=" + symbol);

        return respBody;
    }

    @Override
    public String kline(String symbol, String type, Integer size) {
        return null;
    }

    @Override
    public String accounts() throws Exception {

        // 构造参数签名
        Map<String, String> params = new HashMap<String, String>();
        params.put("api_key", apiKey);
        String sign = CryptoUtil.md5(StringUtil.toQueryString(params) + "&secret_key=" + apiSecret);
        params.put("sign", sign);

        // 发送post请求
        String respBody = HttpUtil.post(API_URL + USERINFO_URL,
                StringUtil.toQueryString(params), "application/x-www-form-urlencoded");

        return respBody;
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
    public String placeOrder(String accountId, String symbol, String price, String amount, String type) throws Exception {
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
        String respBody = HttpUtil.post(API_URL + this.TRADE_URL,
                StringUtil.toQueryString(params), "application/x-www-form-urlencoded");

        return respBody;
    }

    @Override
    public String entrustOrders(String symbol) throws Exception {

        Map<String, String> params = new HashMap<String, String>();
        params.put("api_key", apiKey);
        if(!StringUtils.isEmpty(symbol)){
            params.put("symbol", symbol);
        }
        params.put("status", "1");
        params.put("current_page", "1");
        params.put("page_length", "200");
        String sign = CryptoUtil.md5(StringUtil.toQueryString(params) + "&secret_key=" + apiSecret);
        params.put("sign", sign);

        // 发送post请求
        String respBody = HttpUtil.post(API_URL + this.ORDER_HISTORY_URL, StringUtil.toQueryString(params),"application/x-www-form-urlencoded");

        return respBody;
    }
}
