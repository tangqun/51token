package com.sharex.token.api.currency.resolver;

public class ApiResolverFactory {

    public static IApiResolver getInstence(String exchangeName) {
        switch (exchangeName) {
            case "huobi": return new HuoBiApiResolver();

//            case "okex": return new

            default: return null;
        }
    }

    public static IApiResolver getInstence2(String exchangeName, String apiKey, String apiSecret) {
        switch (exchangeName) {
//            case "huobi": return new HuoBiApiResolver(apiKey, apiSecret);

//            case "okex": return new

            default: return null;
        }
    }
}
