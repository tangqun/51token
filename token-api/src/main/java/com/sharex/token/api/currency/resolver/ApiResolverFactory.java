package com.sharex.token.api.currency.resolver;

public class ApiResolverFactory {

    public static IApiResolver getInstence(String exchangeName) {
        switch (exchangeName) {
            case "huobi": return new HuoBiApiResolver();

//            case "okex": return new

            default: return null;
        }
    }
}
