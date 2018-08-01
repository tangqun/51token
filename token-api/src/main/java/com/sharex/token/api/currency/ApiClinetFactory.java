package com.sharex.token.api.currency;

import com.sharex.token.api.currency.huobi.HuoBiApiClient;

public class ApiClinetFactory {

    public static IApiClient getInstence(String exchangeName) {
        switch (exchangeName) {
            case "huobi": return new HuoBiApiClient();

//            case "okex": return new

            default: return null;
        }
    }
}
