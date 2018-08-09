package com.sharex.token.api.util;

public class ExchangeUtil {

    public static String getSymbol(String exchangeName, String currency) {

        String symbol = null;
        switch (exchangeName) {
            case "huobi": symbol = currency + "usdt"; break;
            case "okex": symbol = currency + "_usdt"; break;
        }
        return symbol;
    }


    public static String getKlineType(String exchangeName, String klineType) {

        //1min   1分钟
        //15min   15分钟
        //30min   30分钟
        //1hour   1小时
        //1day   1天
        //1week   1周
        String resultType = "1min";
        switch (exchangeName) {
            case "huobi":
                switch (klineType) {
                    case "1min": resultType = "1min"; break;
                    case "15min": resultType = "15min"; break;
                    case "30min": resultType = "30min"; break;
                    case "1hour": resultType = "60min"; break;
                    case "1day": resultType = "1day"; break;
                    case "1week": resultType = "1week"; break;
                }
                break;
            case "okex":
                switch (klineType) {

                }
                break;
        }
        return resultType;
    }
}
