package com.sharex.token.api.currency;

import com.sharex.token.api.entity.RESTful;
import com.sharex.token.api.util.HttpUtil;

public class HuoBiApiClient implements IApiClient {

    private static final String API_HOST = "api.huobipro.com";
    private static final String API_URL = "https://" + API_HOST;

    private final String apiKey;
    private final String apiSecret;

    private static final String Ticker_URL = "/market/detail/merged";

    public HuoBiApiClient(String apiKey, String apiSecret) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    @Override
    public RESTful ticker(String symbol) {

        String respBody = HttpUtil.get(API_URL + Ticker_URL + "?symbol=" + symbol);


        return null;
    }


}
