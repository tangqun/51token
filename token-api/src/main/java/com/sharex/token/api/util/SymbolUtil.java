package com.sharex.token.api.util;

public class SymbolUtil {

    public static String getSymbol(String exchangeName, String currency) {

        String symbol = "";
        switch (exchangeName) {
            case "huobi": symbol = currency + "usdt"; break;
            case "okex": symbol = currency + "_usdt"; break;
        }
        return symbol;
    }
}
